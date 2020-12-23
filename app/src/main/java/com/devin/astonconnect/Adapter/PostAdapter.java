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
import com.devin.astonconnect.LoginRegister.StartActivity;
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
import java.util.List;

/** Used to display favourited posts **/
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
        holder.postid = post.getpostid();
        holder.publisherId = post.getpublisher();

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

        //Get bits of information for the current post (publisher info, likes, etc...)
        getPublisherInfo(holder.profile_image, holder.fullname, holder.publisher, post.getpublisher());
        isLiked(post.getpostid(), holder.like);
        numberOfLikes(holder.likeText, post.getpostid());
        getComments(post.getpostid(), holder.comments);
        isBookmarked(post.getpostid(), holder.bookmark);
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }


    private void isLiked(String postid, ImageView imageView){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Likes")
                .child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(firebaseUser.getUid()).exists()){
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
    private void numberOfLikes(TextView likes, String postid){
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

    private void getComments(String postid, final TextView comments){
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

    private void isBookmarked(String postid, ImageView imageView){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Bookmarked")
                .child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(postid).exists()){
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
     * Contains the onclick functionality
     */
    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView profile_image, post_image, like, comment, bookmark;
        public TextView  title, mainDescription, likeText, username, fullname, publisher, bottomDescription, comments;
        public Boolean isImagePost;
        public String postid;
        public String publisherId;


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
            bottomDescription   = itemView.findViewById(R.id.bottomDescription);
            comments            = itemView.findViewById(R.id.comments);

            /** OnClick functionality to save a post **/
            bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (bookmark.getTag().equals("Bookmark")){
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
                    if(like.getTag().equals("like")) { //meaning the user has not liked the post yet
                        FirebaseDatabase.getInstance().getReference().child("Likes").child(postid)
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(true);
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
                    Intent intent = new Intent(mContext, CommentsActivity.class);
                    intent.putExtra("postid", postid);
                    intent.putExtra("publisherid", publisherId);
                    mContext.startActivity(intent);
                }
            });

            comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, CommentsActivity.class);
                    intent.putExtra("postid", postid);
                    intent.putExtra("publisherid", publisherId);
                    mContext.startActivity(intent);
                }
            });

            fullname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("profileid", publisherId);
                    editor.apply();
                    Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_profileFragment);
                }
            });

            profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("profileid", publisherId);
                    editor.apply();
                    Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_profileFragment);
                }
            });

            /**
            post_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("profileid", publisherId);
                    editor.apply();
                    Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_profileFragment);
                }
            });
             **/
        }
    }
}
