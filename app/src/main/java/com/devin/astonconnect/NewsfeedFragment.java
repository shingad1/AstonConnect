package com.devin.astonconnect;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.devin.astonconnect.Adapter.PostAdapter;
import com.devin.astonconnect.Loading.LoadingDialog;
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

    //Posts stuff
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> mPosts;
    private List<String> followingList;
    private ImageView logOutBtn;

    //Floating Action button stuff
    private Animation rotateOpen, rotateClose, fromBottom, toBottom;
    private FloatingActionButton add_btn, createTextPostBtn, createImagePostBtn;
    private Boolean clicked = false; //for animation purposes


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        /** Displaying posts stuff **/
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mPosts = new ArrayList<>();
        followingList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), mPosts);
        recyclerView.setAdapter(postAdapter);

        /** Floating action button stuff **/
        add_btn            = view.findViewById(R.id.add_btn);
        createTextPostBtn = view.findViewById(R.id.createPostBtn);
        createImagePostBtn = view.findViewById(R.id.createImagePostBtn);

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
                mPosts.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    for(String id : followingList){
                        if(post.getpublisher().equals(id)){
                            mPosts.add(post);
                        }
                    }
                }
                postAdapter.notifyDataSetChanged();
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