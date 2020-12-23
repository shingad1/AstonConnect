package com.devin.astonconnect.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Layout;
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

    private Context mContext;
    private List<Post> mFavouritePosts;

    public FavouritePostAdapter(Context mContext, List<Post> mFavouritePosts){
        this.mContext = mContext;
        this.mFavouritePosts = mFavouritePosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.favourite_post_item, parent, false);
        return new FavouritePostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = mFavouritePosts.get(position);
        if(post.getisimagepost()){
            holder.postImage.setVisibility(View.VISIBLE);
            holder.postTitle.setText(post.gettitle());
            holder.postid = post.getpostid();
            Glide.with(mContext).load(post.getpostimage()).into(holder.postImage);
        } else { //then it is a textual post
            holder.postTitle.setVisibility(View.VISIBLE);
            holder.postImage.setVisibility(View.GONE);
            holder.postTitle.setText(post.gettitle());
            holder.postid = post.getpostid();
        }
    }

    @Override
    public int getItemCount() {
        return mFavouritePosts.size();
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
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("postid", postid); //gets passed to selectedpostfragment which populates the adapter (mPosts list) accordingly based on postid
                    editor.apply();
                    Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_selectedPostFragment);
                }
            });

        }
    }
}
