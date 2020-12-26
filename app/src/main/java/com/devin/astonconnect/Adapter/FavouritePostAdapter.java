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
import com.devin.astonconnect.Model.Post;
import com.devin.astonconnect.R;

import java.util.List;

public class FavouritePostAdapter extends RecyclerView.Adapter<FavouritePostAdapter.ViewHolder>{
    /** Used in ProfileFragment **/

    private Context context;
    private List<Post> favouritePostList;

    public FavouritePostAdapter(Context context, List<Post> favouritePostList){
        this.context = context;
        this.favouritePostList = favouritePostList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favourite_post_item, parent, false);
        return new FavouritePostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = favouritePostList.get(position);
        if(post.getIsImagePost()){
            holder.postImage.setVisibility(View.VISIBLE);
            holder.postTitle.setText(post.getTitle());
            holder.postid = post.getPostId();
            Glide.with(context).load(post.getPostImage()).into(holder.postImage);
        } else { //then it is a textual post
            holder.postTitle.setVisibility(View.VISIBLE);
            holder.postImage.setVisibility(View.GONE);
            holder.postTitle.setText(post.getTitle());
            holder.postid = post.getPostId();
        }
    }

    @Override
    public int getItemCount() {
        return favouritePostList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView postTitle;
        public ImageView postImage;
        public String postid;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postTitle = itemView.findViewById(R.id.postTitle);
            postImage = itemView.findViewById(R.id.postImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("postid", postid); //gets passed to selectedpostfragment which populates the adapter (mPosts list) accordingly based on postid
                    editor.apply();
                    Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_selectedPostFragment);
                }
            });

        }
    }
}
