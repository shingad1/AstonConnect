package com.devin.astonconnect.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devin.astonconnect.Model.ActivityItem;
import com.devin.astonconnect.Model.Post;
import com.devin.astonconnect.Model.User;
import com.devin.astonconnect.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityOverviewAdapter extends RecyclerView.Adapter<ActivityOverviewAdapter.ViewHolder> {

    private Context context;
    private List<ActivityItem> activityItemList;

    public ActivityOverviewAdapter(Context context, List<ActivityItem> activityItemList) {
        this.context = context;
        this.activityItemList = activityItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_overview_item, parent, false);
        return new ActivityOverviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActivityItem activityItem = activityItemList.get(position);

        holder.detailsText.setText(activityItem.getDetails());
        holder.ispost = activityItem.getIsPost();
        holder.postid = activityItem.getPostId();
        holder.profileid = activityItem.getUserId();
        getUserDetails(holder.profileImage, holder.fullnameText, activityItem.getUserId());


        if(activityItem.getIsPost()){
            holder.postImage.setVisibility(View.VISIBLE);
            getPostDetails(holder.postImage, activityItem.getPostId());
        } else {
            holder.postImage.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return activityItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView profileImage;
        public ImageView postImage;
        public TextView fullnameText;
        public TextView detailsText;
        public Boolean ispost;
        public String postid;
        public String profileid;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profile_image);
            postImage = itemView.findViewById(R.id.postImage);
            fullnameText = itemView.findViewById(R.id.fullname);
            detailsText = itemView.findViewById(R.id.detailsText);

        /** OnClick functionality for the activity item **/
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ispost){
                    SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("postid", postid);
                    editor.apply();

                    Navigation.findNavController(view).navigate(R.id.action_activityOverviewFragment_to_selectedPostFragment);
                } else {
                    SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("profileid", profileid);
                    editor.apply();

                    Navigation.findNavController(view).navigate(R.id.action_activityOverviewFragment_to_profileFragment);
                }
            }
        });

        }
    }

    private void getUserDetails(ImageView profileImage, TextView fullname, String publisherid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(publisherid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(context).load(user.getImageurl()).into(profileImage);
                fullname.setText(user.getFullname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getPostDetails(ImageView postImage, String postid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                Glide.with(context).load(post.getPostImage()).into(postImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
