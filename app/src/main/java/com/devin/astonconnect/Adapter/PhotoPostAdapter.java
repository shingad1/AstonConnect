package com.devin.astonconnect.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devin.astonconnect.Model.Post;
import com.devin.astonconnect.R;

import java.util.List;

/**
 * Used in the ProfileFragment to display photo post items
 *
 */
public class PhotoPostAdapter extends RecyclerView.Adapter<PhotoPostAdapter.ViewHolder> {

    private Context context;
    private List<Post> postList;

    public PhotoPostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_photo_item, parent, false);
        return new PhotoPostAdapter.ViewHolder(view);
    }

    /**
     * Use the Glide Library to display posts more easily based on the post image URL
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.postid = post.getPostId();
        Glide.with(context).load(post.getPostImage()).into(holder.postImage);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView postImage;
        public String postid;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.postImage);

            postImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    //gets passed to selectedpostfragment which populates the adapter (mPosts list) accordingly based on postid
                    editor.putString("postid", postid);
                    editor.apply();
                    Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_selectedPostFragment);
                }
            });

        }
    }
}
