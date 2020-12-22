package com.devin.astonconnect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.devin.astonconnect.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    private ImageView backBtn, profileImage, viewPhotoPostBtn, viewTextPostBtn;
    private TextView fullname,bioText;
    private Button chatBtn, followBtn;
    private RecyclerView recyclerViewPost, recyclerViewText;
    private FirebaseUser firebaseUser;
    private String profileId;

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
        recyclerViewPost = view.findViewById(R.id.recycler_view_post);
        recyclerViewText = view.findViewById(R.id.recycler_view_text);

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
                    //go to edit profile
                } else if (btnText.equals("follow")){

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(profileId).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId)
                            .child("followers").child(firebaseUser.getUid()).setValue(true);

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
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });

        //Retrieve and set the user information
        getUserInfo();

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
                if(user.getimageurl() != null) { Glide.with(getContext()).load(user.getimageurl()).into(profileImage); }
                fullname.setText(user.getfullname());
                bioText.setText(user.getbio());
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
}