package com.devin.astonconnect.Adapter;

import android.content.Context;
import android.content.Intent;
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
import com.devin.astonconnect.Model.User;
import com.devin.astonconnect.Post.CommentsActivity;
import com.devin.astonconnect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

/**
 * Adapter which is used within the newsfeed fragment. Handles the post interaction along with passing data to the view layer
 **/
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    public Context context;
    public List<Post> postList;
    private FirebaseUser firebaseUser;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        return new PostAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Post post = postList.get(position);

        holder.title.setText(post.getTitle());
        holder.isImagePost = post.getIsImagePost();
        holder.postid = post.getPostId();
        holder.publisherId = post.getPublisher();

        /** Change the visibility of post elements based on whether it is an image post or textual post. **/
        if (post.getIsImagePost() == false) {
            holder.post_image.setVisibility(View.GONE);
            holder.bottomDescription.setVisibility(View.GONE);
            holder.mainDescription.setVisibility(View.VISIBLE);
            holder.mainDescription.setText(post.getDescription());

        } else if (post.getIsImagePost() == true) {
            holder.post_image.setVisibility(View.VISIBLE);
            holder.bottomDescription.setVisibility(View.VISIBLE);
            Glide.with(context).load(post.getPostImage()).into(holder.post_image);
            holder.bottomDescription.setText(post.getDescription());
        }

        //Get bits of information for the current post by performing database calls  (publisher info, likes, etc...)
        //Pass the retrieved information to the viewholder to view
        getPublisherInfo(holder.profile_image, holder.fullname, holder.publisher, post.getPublisher());
        isLiked(post.getPostId(), holder.like);
        numberOfLikes(holder.likeText, post.getPostId());
        getComments(post.getPostId(), holder.comments);
        isBookmarked(post.getPostId(), holder.bookmark);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    /**
     * Checks if the post is liked or not. If it is liked, then change the associated ImageView
     **/
    private void isLiked(String postid, ImageView imageView) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Likes")
                .child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.ic_heart);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.ic_heart_border);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Get the number of likes on the current post
     */
    private void numberOfLikes(TextView likes, String postid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Likes")
                .child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likes.setText(snapshot.getChildrenCount() + " likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    /**
     * Get details about the poster
     */
    private void getPublisherInfo(ImageView profile_image, TextView fullname, TextView publisher, String userid) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid); //Get the database location based on the user id

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(context).load(user.getImageurl()).into(profile_image);
                fullname.setText(user.getFullname());
                publisher.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Count the number of comments for the current post
     */
    private void getComments(String postid, final TextView comments) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments.setText("View all " + snapshot.getChildrenCount() + " comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Check if the post is bookmarked or not.
     * Depending on this, display a different Imageview
     */
    private void isBookmarked(String postid, ImageView imageView) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Bookmarked")
                .child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postid).exists()) {
                    imageView.setImageResource(R.drawable.ic_bookmarked);
                    imageView.setTag("Bookmarked");
                } else {
                    imageView.setImageResource(R.drawable.ic_bookmark_outline);
                    imageView.setTag("Bookmark");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    /**
     * Contains the onclick functionality of post elements. Recieves values from the database calls made above.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView profile_image, post_image, like, comment, bookmark;
        public TextView title, mainDescription, likeText, username, fullname, publisher, bottomDescription, comments;
        public Boolean isImagePost;
        public String postid;
        public String publisherId;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_image = itemView.findViewById(R.id.profile_image);
            post_image = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            bookmark = itemView.findViewById(R.id.bookmark);

            //Textview
            title = itemView.findViewById(R.id.title);
            mainDescription = itemView.findViewById(R.id.mainDescription);
            likeText = itemView.findViewById(R.id.likeText);
            fullname = itemView.findViewById(R.id.fullname);
            publisher = itemView.findViewById(R.id.publisher);
            bottomDescription = itemView.findViewById(R.id.bottomDescription);
            comments = itemView.findViewById(R.id.comments);

            /** OnClick functionality to save a post **/
            bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (bookmark.getTag().equals("Bookmark")) {
                        FirebaseDatabase.getInstance().getReference().child("Bookmarked").child(firebaseUser.getUid())
                                .child(postid).setValue(true);
                    } else {
                        FirebaseDatabase.getInstance().getReference().child("Bookmarked").child(firebaseUser.getUid())
                                .child(postid).removeValue();
                        bookmark.setTag("Bookmark");
                    }
                }
            });


            /** OnClick functionality for the viewholder **/
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (like.getTag().equals("like")) { //meaning the user has not liked the post yet
                        FirebaseDatabase.getInstance().getReference().child("Likes").child(postid)
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(true);

                        addActivityItem(publisherId, postid); //ADDS NOTIFICATION ITEM TO THE LIST

                    } else { //the user has liked the post, clicking like will remove it from the database
                        FirebaseDatabase.getInstance().getReference().child("Likes").child(postid)
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .removeValue();
                    }
                }
            });

            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CommentsActivity.class);
                    intent.putExtra("postid", postid);
                    intent.putExtra("publisherid", publisherId);
                    context.startActivity(intent);
                }
            });

            comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CommentsActivity.class);
                    intent.putExtra("postid", postid);
                    intent.putExtra("publisherid", publisherId);
                    context.startActivity(intent);
                }
            });

            fullname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("profileid", publisherId);
                    editor.apply();

                    if (Navigation.findNavController(view).getCurrentDestination().getId() == R.id.newsfeedFragment) {
                        Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_profileFragment);
                    }
                }
            });

            profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("profileid", publisherId);
                    editor.apply();

                    if (Navigation.findNavController(view).getCurrentDestination().getId() == R.id.newsfeedFragment) {
                        Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_profileFragment);
                    }
                }
            });
        }

        private void addActivityItem(String userid, String postid) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("userid", firebaseUser.getUid());
            hashMap.put("details", "liked your post");
            hashMap.put("postid", postid);
            hashMap.put("ispost", true);

            reference.push().setValue(hashMap);
        }
    }
}
