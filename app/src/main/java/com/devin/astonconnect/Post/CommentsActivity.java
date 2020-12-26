package com.devin.astonconnect.Post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devin.astonconnect.Adapter.CommentAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    private EditText userComment;
    private ImageView profileImage;
    private Button postBtn;
    private ImageView back;
    private String postid;
    private String publisherid;
    private FirebaseUser firebaseUser;


    //Recyclerview stuff
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        userComment  = findViewById(R.id.user_comment);
        profileImage = findViewById(R.id.profile_image);
        postBtn      = findViewById(R.id.postBtn);
        back         = findViewById(R.id.back);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //Recyclerview stuff
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        commentsList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, commentsList);
        recyclerView.setAdapter(commentAdapter);

        //Displaying comment
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();


                //NavController navController = Navigation.findNavController(view);

                //navController.navigateUp();
                //navController.navigate(R.id.newsfeedFragment);

                /**
                startActivity(new Intent(CommentsActivity.this, MainActivity.class));
                finish();
                 **/
            }
        });

        Intent intent = getIntent();
        postid      = intent.getStringExtra("postid"); //from postadapter? when you click comments
        publisherid = intent.getStringExtra("publisherid");


        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userComment.getText().toString().equals("")){
                    Toast.makeText(CommentsActivity.this, "You cannot send an empty comment", Toast.LENGTH_SHORT).show();
                } else {
                    addComment();
                }
            }
        });

        getUserImage();
        getComments();
    }

    private void addComment(){
        //Within the post ID it creates a unique identifier
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postid);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("comment", userComment.getText().toString());
        hashMap.put("publisher", firebaseUser.getUid());
        reference.push().setValue(hashMap);


        addActivityItem();

        userComment.setText("");
    }

    private void getUserImage(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(getApplicationContext()).load(user.getImageurl()).into(profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getComments(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentsList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Comment comment = snapshot.getValue(Comment.class);
                    commentsList.add(comment);
                    Log.w("Comment", comment.getComment());
                }
                commentAdapter.notifyDataSetChanged();
                System.out.println("Number of comments" + String.valueOf(commentsList.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addActivityItem(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(publisherid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("details", "Commented: " + userComment.getText().toString());
        hashMap.put("postid", postid);
        hashMap.put("ispost", true);

        reference.push().setValue(hashMap);
    }
}