package com.devin.astonconnect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.devin.astonconnect.Adapter.FavouritePostAdapter;
import com.devin.astonconnect.Adapter.PhotoPostAdapter;
import com.devin.astonconnect.Adapter.TextPostAdapter;
import com.devin.astonconnect.Chat.MessagingActivity;
import com.devin.astonconnect.Model.Post;
import com.devin.astonconnect.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class ProfileFragment extends Fragment {

    private ImageView backBtn, profileImage;
    private RelativeLayout viewPhotoPostBtn, viewTextPostBtn;
    private TextView fullname,bioText;
    private Button chatBtn, followBtn;
    private RecyclerView recyclerViewPost, recyclerViewText, recyclerViewFavourites;
    private FirebaseUser firebaseUser;
    private String profileId;
    private PhotoPostAdapter photoPostAdapter;
    private List<Post> imagePostList;

    //text post  stuff
    private TextPostAdapter textPostAdapter;
    private List<Post> textPostList;

    //favourited posts stuff
    private FavouritePostAdapter favouritePostAdapter;
    private List<Post> favouritePostList;
    private List<String> savedPostList; //temporarily holds the keys of the saved posts, retrieves them using this list and then populates adapter
    private Boolean isStaff = false;

    //Views to show different colours for when a button is clicked / not
    private View postsBackgroundUnSelected, postsBackgroundSelected, textPostsUnSelected, textPostsSelected;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseUser     = FirebaseAuth.getInstance().getCurrentUser();
        backBtn          = view.findViewById(R.id.backBtn);
        profileImage     = view.findViewById(R.id.profile_image);
        viewPhotoPostBtn = view.findViewById(R.id.viewPhotoPostBtn);
        viewTextPostBtn  = view.findViewById(R.id.viewTextPostBtn);
        fullname         = view.findViewById(R.id.fullname);
        bioText          = view.findViewById(R.id.bioText);
        chatBtn          = view.findViewById(R.id.chatBtn);
        followBtn        = view.findViewById(R.id.followBtn);
        recyclerViewText = view.findViewById(R.id.recycler_view_text);

        //Views for changign colour based on click
        postsBackgroundUnSelected = view.findViewById(R.id.postsBackgroundUnSelected);
        postsBackgroundSelected = view.findViewById(R.id.postsBackgroundSelected);
        textPostsUnSelected = view.findViewById(R.id.textPostsUnSelected);
        textPostsSelected = view.findViewById(R.id.textPostsSelected);



        //Recyclerview stuff showing mixed image and text posts for favourites
        recyclerViewFavourites = view.findViewById(R.id.recycler_view_favourites);
        recyclerViewFavourites.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager3 = new GridLayoutManager(getContext(), 3);
        recyclerViewFavourites.setLayoutManager(linearLayoutManager3);
        favouritePostList = new ArrayList<>();
        favouritePostAdapter = new FavouritePostAdapter(getContext(), favouritePostList);
        recyclerViewFavourites.setAdapter(favouritePostAdapter);


        //Recyclerview stuff showing image posts
        recyclerViewPost = view.findViewById(R.id.recycler_view_post);
        recyclerViewPost.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerViewPost.setLayoutManager(linearLayoutManager);
        imagePostList = new ArrayList<>();
        photoPostAdapter = new PhotoPostAdapter(getContext(), imagePostList);
        recyclerViewPost.setAdapter(photoPostAdapter);


        //RecyclerView stuff showing text posts
        recyclerViewText = view.findViewById(R.id.recycler_view_text);
        recyclerViewText.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
        recyclerViewText.setLayoutManager(linearLayoutManager1);
        textPostList = new ArrayList<>();
        textPostAdapter = new TextPostAdapter(getContext(), textPostList);
        recyclerViewText.setAdapter(textPostAdapter);


        //Get the profile id of the user, passed in from the mainactviity newsfeedfragment
        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileId = prefs.getString("profileid", "none");

        /**If the profileid is the current user's ID then they will be able to see edit profile, instead of chat (Chat -> Edit Profile) */
        if(profileId.equals(firebaseUser.getUid())){
            chatBtn.setText("Edit Profile");
            followBtn.setText("Favourite posts");
        } else {
           checkFollow(); //Check to see if the OTHER user follows you or not, and based on that change the follow button
        }

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btnText = chatBtn.getText().toString();

                if(btnText.equals("Edit Profile")){
                    startActivity(new Intent(getContext(), ProfileEditActivity.class));
                } else if (btnText.equals("Chat")){
                    Intent intent = new Intent(getContext(), MessagingActivity.class);
                    intent.putExtra("userid", profileId);
                    startActivity(intent);
                }
            }
        });

        //view favourited posts
        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btnText = followBtn.getText().toString();

                if(followBtn.getText().equals("Favourite posts")){ //meaning you are viewing your own profile
                    recyclerViewText.setVisibility(View.GONE);
                    recyclerViewPost.setVisibility(View.GONE);
                    recyclerViewFavourites.setVisibility(View.VISIBLE);
                } else if (btnText.equals("follow")){

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(profileId).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId)
                            .child("followers").child(firebaseUser.getUid()).setValue(true);

                    addActivityItem();//Set activity item

                } else if (btnText.equals("following")){

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(profileId).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId)
                            .child("followers").child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        //normal stuff
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
                //startActivity(new Intent(getActivity(), MainActivity.class));
                //getActivity().finish();
            }
        });

        viewPhotoPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postsBackgroundUnSelected.setVisibility(View.GONE);
                textPostsSelected.setVisibility(View.GONE);
                textPostsUnSelected.setVisibility(View.VISIBLE);
                postsBackgroundSelected.setVisibility(View.VISIBLE);


                recyclerViewPost.setVisibility(View.VISIBLE);
                recyclerViewText.setVisibility(View.GONE);
                recyclerViewFavourites.setVisibility(View.GONE);
            }
        });

        viewTextPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postsBackgroundUnSelected.setVisibility(View.VISIBLE);
                textPostsSelected.setVisibility(View.VISIBLE);
                textPostsUnSelected.setVisibility(View.GONE);
                postsBackgroundSelected.setVisibility(View.GONE);


                recyclerViewText.setVisibility(View.VISIBLE);
                recyclerViewPost.setVisibility(View.GONE);
                recyclerViewFavourites.setVisibility(View.GONE);
            }
        });

        //Retrieve and set the user information
        getUserInfo();
        //Load the user's photo posts to populate the adapter called 'PhotoPostAdapter' and adapter caled 'TextPostAdapter'
        getPosts();
        //Get saved posts for favourited posts
        readSavedPosts();

        return view;
    }

    private void getUserInfo(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(profileId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (getContext() == null) {
                    return;
                }
                User user = snapshot.getValue(User.class);
                System.out.println(profileId);
                String profileid = profileId;
                if(user.getImageurl() != null) { Glide.with(getContext()).load(user.getImageurl()).into(profileImage); }
                fullname.setText(user.getFullname());
                bioText.setText(user.getBio());
                isStaff = user.getisStaff();

                //Hide the chat button if the other person is a member of CS Staff and it is someone elses profile
                //If the member of CS Staff were to view their own profile, it would show 'Edit Profile'
                if(!profileId.equals(firebaseUser.getUid())){
                    if(isStaff){
                        chatBtn.setVisibility(View.GONE);
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Checks to see if the 'profileid' is currently followed by the current user
    //This method is only triggered if the user is looking at another user's profile, and not their own.
    private void checkFollow(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(profileId).exists()){
                    followBtn.setText("following");
                } else {
                    followBtn.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getPosts(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imagePostList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if(post.getIsImagePost() == true){
                        if (post.getPublisher().equals(profileId)) {
                            imagePostList.add(post);
                        }
                    } else if (post.getIsImagePost() == false){
                        if (post.getPublisher().equals(profileId)){
                            textPostList.add(post);
                        }
                    }
                }
                Collections.reverse(imagePostList); //show the latest first
                photoPostAdapter.notifyDataSetChanged();

                Collections.reverse(textPostList); //show the latest first
                textPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readSavedPosts(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bookmarked").child(firebaseUser.getUid());
        savedPostList = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    savedPostList.add(snapshot.getKey());
                }
                getSavedPosts(); //have the ID's of the saved posts, now need to retrieve the actual posts..and add them to the favouritedPosts list
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getSavedPosts(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favouritePostList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    for(String id :savedPostList){
                        if (post.getPostId().equals(id)) {
                            favouritePostList.add(post);
                        }
                    }
                }
                favouritePostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addActivityItem(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(profileId);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("details", "Is now following you");
        hashMap.put("postid", "");
        hashMap.put("ispost", false);

        reference.push().setValue(hashMap);
    }
}