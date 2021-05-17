package com.devin.astonconnect.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.devin.astonconnect.MainActivity;
import com.devin.astonconnect.Model.Comment;
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

/**
 * Comment Adapter class used within the CommenmtActivity
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{

    private Context context;
    private List<Comment> commentsList;
    private FirebaseUser firebaseUser;

    public CommentAdapter(Context context, List<Comment> commentsList){
        this.context = context;
        this.commentsList = commentsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }

    /**
     * Sets viewholder values based on the data passed from the CommentsActivity
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Comment comment = commentsList.get(position);

        holder.comment.setText(comment.getComment());
        //Retrieves the user details based on the comment.getpublisher() value.
        getUserDetails(holder.profile_image, holder.fullname, comment.getPublisher());

        holder.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("publisherid", comment.getPublisher());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    private void getUserDetails(ImageView imageView, TextView fullname, String publisherid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(publisherid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(context).load(user.getImageurl()).into(imageView);
                fullname.setText(user.getFullname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView profile_image;
        public TextView fullname, comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_image = itemView.findViewById(R.id.profile_image);
            fullname      = itemView.findViewById(R.id.fullname);
            comment       = itemView.findViewById(R.id.comment);

        }
    }
}
