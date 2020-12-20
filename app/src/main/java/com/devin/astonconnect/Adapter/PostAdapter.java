package com.devin.astonconnect.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devin.astonconnect.Model.Post;
import com.devin.astonconnect.Model.User;
import com.devin.astonconnect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    public  Context mContext;
    public  List<Post> mPost;
    private FirebaseUser firebaseUser;

    public PostAdapter(Context mContext, List<Post> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);
        return new PostAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Post post = mPost.get(position);

        holder.title.setText(post.gettitle());
        holder.isImagePost = post.getisimagepost();

        /** Change the visibility of the post (viewholder) based on values **/
        if(post.getisimagepost() == false){
            holder.post_image.setVisibility(View.GONE);
            holder.bottomDescription.setVisibility(View.GONE);
            holder.mainDescription.setVisibility(View.VISIBLE);
            holder.mainDescription.setText(post.getdescription());

        } else if(post.getisimagepost() == true){

            holder.post_image.setVisibility(View.VISIBLE);
            holder.bottomDescription.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(post.getpostimage()).into(holder.post_image);
            holder.bottomDescription.setText(post.getdescription());
        }

        getPublisherInfo(holder.profile_image, holder.fullname, holder.publisher, post.getpublisher());
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }


    private void getPublisherInfo(ImageView profile_image,  TextView fullname, TextView publisher, String userid){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid); //Get the database location based on the user id

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(mContext).load(user.getimageurl()).into(profile_image);
                fullname.setText(user.getfullname());
                publisher.setText(user.getusername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView profile_image, post_image, like, comment, bookmark;
        public TextView  title, mainDescription, likeText, username, fullname, publisher, bottomDescription, comments;
        public Boolean isImagePost;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_image = itemView.findViewById(R.id.profile_image);
            post_image    = itemView.findViewById(R.id.post_image);
            like          = itemView.findViewById(R.id.like);
            comment       = itemView.findViewById(R.id.comment);
            bookmark      = itemView.findViewById(R.id.bookmark);

            //Textview
            title               = itemView.findViewById(R.id.title);
            mainDescription     = itemView.findViewById(R.id.mainDescription);
            likeText            = itemView.findViewById(R.id.likeText);
            fullname            = itemView.findViewById(R.id.fullname);
            publisher           = itemView.findViewById(R.id.publisher);
            bottomDescription = itemView.findViewById(R.id.bottomDescription);
            comments            = itemView.findViewById(R.id.comments);
        }
    }

}
