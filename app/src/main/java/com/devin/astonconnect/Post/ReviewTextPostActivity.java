package com.devin.astonconnect.Post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.devin.astonconnect.Loading.LoadingDialog;
import com.devin.astonconnect.MainActivity;
import com.devin.astonconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ReviewTextPostActivity extends AppCompatActivity {

    private ImageView close;
    private Button postBtn, cancelBtn;
    private EditText description, title;
    private LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_text_post);

        loadingDialog = new LoadingDialog(ReviewTextPostActivity.this, R.layout.custom_loading_dialog);


        close = findViewById(R.id.close);
        postBtn = findViewById(R.id.postButton);
        cancelBtn = findViewById(R.id.cancel_button);
        title     = findViewById(R.id.title);
        description = findViewById(R.id.description);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReviewTextPostActivity.this, MainActivity.class));
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReviewTextPostActivity.this, MainActivity.class));
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(title.getText().toString()) || TextUtils.isEmpty(description.getText().toString())){
                    Toast.makeText(ReviewTextPostActivity.this, "Please make sure you have filled in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    publishPost();
                }
            }
        });
    }

    public void publishPost(){
        loadingDialog.startLoadingAnimation();

        //TODO: Potentially change this to be nested within a user parent element in the database. USER ID > POST ID > post details.
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        String postid = reference.push().getKey();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("postid", postid);
        hashMap.put("title", title.getText().toString());
        hashMap.put("postImage", "null");
        hashMap.put("description", description.getText().toString());
        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("isImagePost", false);

        reference.child(postid).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(ReviewTextPostActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(ReviewTextPostActivity.this, "Something went wrong. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}