package com.devin.astonconnect.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import de.hdodenhof.circleimageview.CircleImageView;

/** This class allows users to send messages to each other **/
public class MessagingActivity extends AppCompatActivity {

    //Fields for displaying user information
    private ImageView backButton;
    private CircleImageView userImage;
    private TextView userName;

    //Fields for user input
    private EditText userMessage;
    private ImageView sendButton;


    FirebaseUser firebaseUser; //The current user using the app
    DatabaseReference databaseReference;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        //display user info
        backButton = findViewById(R.id.backButton);
        userImage  = findViewById(R.id.userImage);
        userName   = findViewById(R.id.usernameText);

        //Controls
        userMessage = findViewById(R.id.userMessage);
        sendButton  = findViewById(R.id.sendButton);

        //Recieving the user id
        intent     = getIntent();
        String userid = intent.getStringExtra("userid"); //passed from the profilefragment when the chat button is clicked

        //Firebase user (current user)
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        //Get the details based on the 'userid' parameter
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                userName.setText(user.getFullname());
                if(user.getImageurl() != null){
                    Glide.with(MessagingActivity.this).load(user.getImageurl()).into(userImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Send functionality - make use of the send message function.
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = userMessage.getText().toString();
                if(!userMessage.equals("")){
                    sendMessage(firebaseUser.getUid(), userid, message);
                    Log.w("Sent message:", message);
                    System.out.println("Sent message: " + message);
                } else {
                    Toast.makeText(MessagingActivity.this, "Can't send empty messages!", Toast.LENGTH_SHORT).show();
                }
                userMessage.setText(""); //reset
            }
        });
    }

    /** COULD further optimise the messaging process through storing it inside of another collection? **/
    private void sendMessage(String senderid, String recieverid, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("senderid", senderid);
        hashMap.put("recieverid", recieverid);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);
    }
}