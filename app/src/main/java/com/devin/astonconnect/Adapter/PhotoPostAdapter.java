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

public class PhotoPostAdapter extends RecyclerView.Adapter<PhotoPostAdapter.ViewHolder>{

    private Context mContext;
    private List<Post> mPosts;

    public PhotoPostAdapter(Context mContext, List<Post> mPosts){
        this.mContext = mContext;
        this.mPosts   = mPosts;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_photo_item, parent, false);
        return new PhotoPostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = mPosts.get(position);
        Glide.with(mContext).load(post.getpostimage()).into(holder.postImage);


        /** Opening post in full from clicking on it's image **/
        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("postid", post.getpostid());
                editor.apply();

                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_selectedPostFragment);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView postImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postImage = itemView.findViewById(R.id.postImage);

        }
    }
}
