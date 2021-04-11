package com.devin.astonconnect.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devin.astonconnect.Model.User;
import com.devin.astonconnect.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context context;
    private List<User> userList;
    private FirebaseUser currentUser;

    public UserAdapter(Context context, List<User> userList){
        this.context = context;
        this.userList = userList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final User user = userList.get(position);

        holder.btn_follow.setVisibility(View.VISIBLE);
        //holder.username.setText(user.getUsername());
        holder.fullname.setText(user.getFullname());
        //holder.bio.setText(user.getBio());
        Glide.with(context).load(user.getImageurl()).into(holder.image_profile);

        //Check to see if the user is being followed by the logged in user (currentUser) and if so, set the button text accordingly
        isFollowing(user.getId(), holder.followText, holder.personIcon, holder.btn_follow);

        //set the follow button to be invisible if the selected user is the same as the logged in user.
        //could remove form the list in the future?
        if(user.getId().equals(currentUser.getUid())){
            holder.btn_follow.setVisibility(View.GONE);
        }

        if(user.getisStaff()){
            holder.userType.setText("CS Staff");
        } else {
            holder.userType.setText("Student");
        }

        //Get the user's interests
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getId()).child("interests");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if (holder.chipGroup.getChildCount() == 0) { //If the chip count is 0, then display them only.
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String interest = snapshot.getValue(String.class);
                            Toast.makeText(context, interest, Toast.LENGTH_SHORT).show();
                            holder.interests.add(interest);

                            //Add Chip
                            Chip chip_item = (Chip) LayoutInflater.from(context).inflate(R.layout.chip_item2, null, false);
                            chip_item.setTextSize(15f);
                            chip_item.setText(interest);
                            holder.chipGroup.addView(chip_item);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", user.getId());
                editor.apply();

                Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_profileFragment);
                /** OLD WAY BELOW **/
                //((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                //        new ProfileFragment()).commit();
            }
        });

        holder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.followText.getText().toString().equals("follow")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(currentUser.getUid())
                            .child("following").child(user.getId()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                            .child("followers").child(currentUser.getUid()).setValue(true);

                    //Triggers activity overview item
                    addActivityItem(user.getId());
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(currentUser.getUid())
                            .child("following").child(user.getId()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                            .child("followers").child(currentUser.getUid()).removeValue();
                }
            }
        });



    }

    private void addActivityItem(String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("details", "Is now following you");
        hashMap.put("postid", "");
        hashMap.put("ispost", false);

        reference.push().setValue(hashMap);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //public TextView username;
        public TextView fullname;
        public TextView bio;
        public CircleImageView image_profile;
        public RelativeLayout btn_follow;
        private TextView followText;
        private ImageView personIcon;
        private List<String> interests = new ArrayList<>();
        private ChipGroup chipGroup;
        private TextView userType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

           // username      = itemView.findViewById(R.id.username);
            fullname      = itemView.findViewById(R.id.fullname);
           // bio           = itemView.findViewById(R.id.bio);
            image_profile = itemView.findViewById(R.id.image_profile);
            btn_follow    = itemView.findViewById(R.id.btn_follow);
            followText    = itemView.findViewById(R.id.followText);
            personIcon    = itemView.findViewById(R.id.personIcon);
            chipGroup     = itemView.findViewById(R.id.chipGroup);
            userType      = itemView.findViewById(R.id.userType);
        }

    }

    private void isFollowing(String userid, TextView followText, ImageView personIcon, RelativeLayout btn_follow){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(currentUser.getUid()).child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(userid).exists()){
                    followText.setText("following");
                    personIcon.setImageResource(R.drawable.ic_person);
                } else {
                    followText.setText("follow");
                    personIcon.setImageResource(R.drawable.ic_person_add);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
