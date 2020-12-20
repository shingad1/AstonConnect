package com.devin.astonconnect.Post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devin.astonconnect.LoginRegister.StartActivity;
import com.devin.astonconnect.MainActivity;
import com.devin.astonconnect.Model.User;
import com.devin.astonconnect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CommentsActivity extends AppCompatActivity {

    private EditText userComment;
    private ImageView profileImage;
    private Button postBtn;
    private ImageView back;
    private String postid;
    private String publisherid;

    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        userComment  = findViewById(R.id.user_comment);
        profileImage = findViewById(R.id.profile_image);
        postBtn      = findViewById(R.id.postBtn);
        back         = findViewById(R.id.back);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CommentsActivity.this, MainActivity.class));
                finish();
            }
        });

        Intent intent = getIntent();
        postid      = intent.getStringExtra("postid");
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
    }

    private void addComment(){
        //Within the post ID it creates a unique identifier
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postid);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("comment", userComment.getText().toString());
        hashMap.put("publisher", firebaseUser.getUid());
        reference.push().setValue(hashMap);

        userComment.setText("");
    }

    private void getUserImage(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(getApplicationContext()).load(user.getimageurl()).into(profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}