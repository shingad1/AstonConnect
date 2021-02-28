package com.devin.astonconnect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.devin.astonconnect.Adapter.PostAdapter;
import com.devin.astonconnect.LoginRegister.StartActivity;
import com.devin.astonconnect.Model.Post;
import com.devin.astonconnect.Post.ReviewImagePostActivity;
import com.devin.astonconnect.Post.ReviewTextPostActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NewsfeedFragment extends Fragment {

    //Switch to toggle between showing of both recyclerviews
    private Switch changePostSwitch;

    //Staff post stuff
    private RecyclerView studentPostRecyclerView;
    private List<Post> staffPostList;
    private PostAdapter staffPostAdapter;

    //Student post stuff
    private RecyclerView staffPostRecyclerView;
    private PostAdapter studentPostAdapter;
    private List<Post> studentPostList;
    private List<String> followingList;
    private ImageView logOutBtn, profile;

    //Floating Action button stuff
    private Animation rotateOpen, rotateClose, fromBottom, toBottom;
    private FloatingActionButton add_btn, createTextPostBtn, createImagePostBtn;
    private Boolean clicked = false; //for animation purposes


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        /** Staff post recyclerview **/
        staffPostRecyclerView = view.findViewById(R.id.staffPostRecyclerView);
        staffPostRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        staffPostRecyclerView.setLayoutManager(manager);
        staffPostList = new ArrayList<>();
        staffPostAdapter = new PostAdapter(getContext(), staffPostList);
        staffPostRecyclerView.setAdapter(staffPostAdapter);

        /** Displaying posts stuff **/
        studentPostRecyclerView = view.findViewById(R.id.studentPostRecyclerView);
        studentPostRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        studentPostRecyclerView.setLayoutManager(linearLayoutManager);
        studentPostList = new ArrayList<>();
        followingList = new ArrayList<>();
        studentPostAdapter = new PostAdapter(getContext(), studentPostList);
        studentPostRecyclerView.setAdapter(studentPostAdapter);


        /** Toggle button for showing one recyclerview over another **/
        changePostSwitch = view.findViewById(R.id.changePostSwitch);
        changePostSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                Toast.makeText(getActivity(), "Toggle changed to: " + isChecked, Toast.LENGTH_SHORT).show();
                if(isChecked){
                    staffPostRecyclerView.setVisibility(View.VISIBLE);
                    studentPostRecyclerView.setVisibility(View.GONE);
                }

                if(!isChecked){
                    studentPostRecyclerView.setVisibility(View.VISIBLE);
                    staffPostRecyclerView.setVisibility(View.GONE);
                }
            }
        });


        /** Floating action button stuff **/
        add_btn            = view.findViewById(R.id.add_btn);
        createTextPostBtn = view.findViewById(R.id.createPostBtn);
        createImagePostBtn = view.findViewById(R.id.createImagePostBtn);

        //display profile stuff
        profile = view.findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                editor.apply();
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_profileFragment);
            }
        });

        //logout btn
        logOutBtn = view.findViewById(R.id.logOutBtn);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), StartActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        //Animations for the buttons
        rotateOpen  = AnimationUtils.loadAnimation((getActivity()), R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation((getActivity()), R.anim.rotate_close_anim);
        fromBottom  = AnimationUtils.loadAnimation((getActivity()), R.anim.from_bottom_anim);
        toBottom    = AnimationUtils.loadAnimation((getActivity()), R.anim.to_bottom_anim);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddButtonClicked(); //Triggers the animation
            }
        });

        createTextPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReviewTextPostActivity.class);
                startActivity(intent);
            }
        });

        createImagePostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReviewImagePostActivity.class);
                startActivity(intent);
            }
        });

        checkFollowing();
        return view;
    }


    private void checkFollowing(){
        followingList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    followingList.add(snapshot.getKey());
                }
                readPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void readPosts(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentPostList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    for(String id : followingList){
                        if(post.getPublisher().equals(id)){
                            studentPostList.add(post);
                            staffPostList.add(post); //temporary
                        }
                    }
                }
                studentPostAdapter.notifyDataSetChanged();
                staffPostAdapter.notifyDataSetChanged(); //temporary
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void onAddButtonClicked() {
        setVisibility(clicked);
        setAnimation(clicked);
        clicked = !clicked;
    }

    private void setVisibility(Boolean clicked){
        if(!clicked){
            createTextPostBtn.setVisibility(View.VISIBLE);
            createImagePostBtn.setVisibility(View.VISIBLE);
        } else {
            createTextPostBtn.setVisibility(View.INVISIBLE);
            createImagePostBtn.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimation(Boolean clicked){
        if(!clicked){
            createTextPostBtn.startAnimation(fromBottom);
            createImagePostBtn.startAnimation(fromBottom);
            add_btn.startAnimation(rotateOpen);
        } else {
            createTextPostBtn.startAnimation(toBottom);
            createImagePostBtn.startAnimation(toBottom);
            add_btn.startAnimation(rotateClose);
        }
    }
}