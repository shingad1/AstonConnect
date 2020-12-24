package com.devin.astonconnect.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.devin.astonconnect.Model.Post;
import com.devin.astonconnect.R;

import java.util.List;

public class TextPostAdapter extends RecyclerView.Adapter<TextPostAdapter.ViewHolder>{

    private Context mContext;
    private List<Post> mTextPosts;

    public TextPostAdapter(Context mContext, List<Post> mPosts){
        this.mContext = mContext;
        this.mTextPosts = mPosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_textual_item, parent, false);
        return new TextPostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = mTextPosts.get(position);
        holder.postTitle.setText(post.getTitle());
        holder.postid = post.getPostId();
    }

    @Override
    public int getItemCount() {
        return mTextPosts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView postTitle;
        public String postid;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postTitle = itemView.findViewById(R.id.postTitle);

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
