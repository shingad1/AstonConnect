package com.devin.astonconnect;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.devin.astonconnect.Adapter.PostAdapter;
import com.devin.astonconnect.LoginRegister.StartActivity;
import com.devin.astonconnect.Model.Post;
import com.devin.astonconnect.Post.ReviewImagePostActivity;
import com.devin.astonconnect.Post.ReviewTextPostActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS;
import static com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL;

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

    private SharedPreferencesManager sharedPreferencesManager;

    //LinearLayouts for no posts animation
    private LinearLayout noPostsLayout1;
    private LottieAnimationView searchingAnimation;
    private LinearLayout noPostsLayout2;
    private LottieAnimationView searchingAnimation2;
    private Boolean switchChecked = false;


    //MaterialToolbar - Set the custom scrolling functionality for when there are no posts
    private MaterialToolbar bar;
    private AppBarLayout.LayoutParams params;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        /**Get local user details (shared preferences) **/
        sharedPreferencesManager = new SharedPreferencesManager(getActivity().getApplicationContext());

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

        //Animations and no post stuff
        noPostsLayout1 = view.findViewById(R.id.noPostsLayout1);
        noPostsLayout2 = view.findViewById(R.id.noPostsLayout2);
        searchingAnimation = view.findViewById(R.id.searchingAnimation);
        searchingAnimation2 = view.findViewById(R.id.searchingAnimation2);

        //MaterialToolbar
        bar = view.findViewById(R.id.bar);
        params = (AppBarLayout.LayoutParams) bar.getLayoutParams();

        /** Toggle button for showing one recyclerview over another **/
        changePostSwitch = view.findViewById(R.id.changePostSwitch);
        changePostSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                Toast.makeText(getActivity(), "Toggle changed to: " + isChecked, Toast.LENGTH_SHORT).show();
                if (isChecked) {
                    staffPostRecyclerView.setVisibility(View.VISIBLE);
                    studentPostRecyclerView.setVisibility(View.GONE);
                    switchChecked = isChecked;

                    if (staffPostAdapter.getItemCount() == 0) {
                        noPostsLayout2.setVisibility(View.VISIBLE);
                        searchingAnimation2.setAnimation(getRandomAnimationFile());
                        searchingAnimation2.setSpeed(1);
                        searchingAnimation2.playAnimation();

                        //Disable toolbar scrolling
                        params.setScrollFlags(0);
                        bar.setLayoutParams(params);

                    } else {
                        //Enable toolbar scrolling
                        noPostsLayout2.setVisibility(View.GONE);
                        params.setScrollFlags(SCROLL_FLAG_SCROLL | SCROLL_FLAG_ENTER_ALWAYS);
                        bar.setLayoutParams(params);
                    }

                }

                if (!isChecked) {
                    studentPostRecyclerView.setVisibility(View.VISIBLE);
                    staffPostRecyclerView.setVisibility(View.GONE);
                    switchChecked = !isChecked;
                    noPostsLayout2.setVisibility(View.GONE);
                }
            }
        });


        /** Floating action button stuff **/
        add_btn = view.findViewById(R.id.add_btn);
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
                sharedPreferencesManager.clearPrefs(); //clear the locally stored user data
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), StartActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        //Animations for the buttons
        rotateOpen = AnimationUtils.loadAnimation((getActivity()), R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation((getActivity()), R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation((getActivity()), R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation((getActivity()), R.anim.to_bottom_anim);

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


    private void checkFollowing() {
        followingList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    followingList.add(snapshot.getKey());
                }
                //Read posts to populate recyclerviews
                readStudentPosts();
                readStaffPosts();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void readStudentPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentPostList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    for (String id : followingList) {
                        if (post.getPublisher().equals(id)) {
                            if (post.getPosttype().equals("student")) {
                                studentPostList.add(post);
                            }
                        }
                    }
                }
                studentPostAdapter.notifyDataSetChanged();

                if (!changePostSwitch.isChecked()) {
                    if (studentPostAdapter.getItemCount() == 0) {
                        noPostsLayout1.setVisibility(View.VISIBLE);
                        searchingAnimation.setAnimation(getRandomAnimationFile());
                        //disable toolbar scrolling
                        params.setScrollFlags(0);
                        bar.setLayoutParams(params);
                    } else {
                        params.setScrollFlags(SCROLL_FLAG_SCROLL | SCROLL_FLAG_ENTER_ALWAYS);
                        bar.setLayoutParams(params);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void readStaffPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                staffPostList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    for (String id : followingList) {
                        if (post.getPublisher().equals(id)) {
                            if (post.getPosttype().equals("staff")) {
                                staffPostList.add(post);
                            }
                        }
                    }
                }
                staffPostAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private int getRandomAnimationFile() {
        Random random = new Random();
        int randomInt = random.nextInt(4);
        int id;
        switch (randomInt) {
            case 0:
                id = R.raw.searching_1;
                break;
            case 1:
                id = R.raw.searching_2;
            case 2:
                id = R.raw.searching_3;
                break;
            case 3:
                id = R.raw.searching_4;
            default:
                id = R.raw.searching_1;
        }
        return id;
    }

    private void onAddButtonClicked() {
        setVisibility(clicked);
        setAnimation(clicked);
        clicked = !clicked;
    }

    private void setVisibility(Boolean clicked) {
        if (!clicked) {
            createTextPostBtn.setVisibility(View.VISIBLE);
            createImagePostBtn.setVisibility(View.VISIBLE);
        } else {
            createTextPostBtn.setVisibility(View.INVISIBLE);
            createImagePostBtn.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimation(Boolean clicked) {
        if (!clicked) {
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