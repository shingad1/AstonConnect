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

/** Used in displaying textual post items on the user profile page (not for favorurite posts) **/
public class TextPostAdapter extends RecyclerView.Adapter<TextPostAdapter.ViewHolder>{

    private Context context;
    private List<Post> textPostList;

    public TextPostAdapter(Context context, List<Post> mPosts){
        this.context = context;
        this.textPostList = mPosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_textual_item, parent, false);
        return new TextPostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = textPostList.get(position);
        holder.postTitle.setText(post.getTitle());
        holder.postDescription.setText(post.getDescription());
        holder.postid = post.getPostId();
    }

    @Override
    public int getItemCount() {
        return textPostList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView postTitle;
        public TextView postDescription;
        public String postid;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postTitle = itemView.findViewById(R.id.postTitle);
            postDescription = itemView.findViewById(R.id.postDescription);


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
