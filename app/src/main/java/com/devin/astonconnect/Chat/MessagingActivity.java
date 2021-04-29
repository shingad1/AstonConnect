package com.devin.astonconnect.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    private FirebaseUser firebaseUser; //The current user using the app
    private DatabaseReference databaseReference;
    private Intent intent;

    //Message seen stuff
    private ValueEventListener messageSeenListener;
    private DatabaseReference reference;


    //MessageAdapter stuff
    private MessageAdapter messageAdapter;
    private List<Chat> userChats;
    private RecyclerView recyclerView;

    //UserId of the other person you are talking to
    private String userid;

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

        //Recieving the user id from the profilefragment
        intent     = getIntent();
        userid = intent.getStringExtra("userid"); //passed from the profilefragment when the chat button is clicked (this is the other guy's ID)

        //Recyclerview stuff
        recyclerView = findViewById(R.id.messagesRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        /** Messageadapter stuff handled in the recieveMessages function **/

        //Firebase user (current user)
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);


        //Get the details based on the 'userid' parameter (details of the other user)
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                userName.setText(user.getFullname());
                if(user.getImageurl() != null){
                    Glide.with(getApplicationContext()).load(user.getImageurl()).into(userImage);
                }
               // recieveMessages(firebaseUser.getUid(), userid, user.getImageurl()); //get the chats based on ID information passed
                recieveMessages(firebaseUser.getUid(), userid); //get the chats based on ID information passed
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
                if(!message.equals("")){
                    sendMessage(firebaseUser.getUid(), userid, message);
                    Log.w("Sent message:", message);
                    System.out.println("Sent message: " + message);
                } else {
                    Toast.makeText(MessagingActivity.this, "Can't send empty messages!", Toast.LENGTH_SHORT).show();
                }
                userMessage.setText(""); //reset
            }
        });

        seenMessage(userid);
    }


    private void sendMessage(String senderid, String recieverid, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("senderid", senderid);
        hashMap.put("recieverid", recieverid);
        hashMap.put("message", message);
        hashMap.put("messageseen", false);

        reference.child("Chats").push().setValue(hashMap);

        /**
         * Set up a new ChatList Reference if one does not exist already.
         * First, set up the DatabaseReference using the current User's ID and with the other participants ID as the child.
         * Then if the dataSnapshot does not exist, then set the chatReference value.
         */
        DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(firebaseUser.getUid())
                .child(userid);

        chatReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    chatReference.child("id").setValue(userid); //set the value of the other person that the user is talking to
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Set the ChatList reference for the other user too (so that they can open the chat)
        DatabaseReference chatReference2 = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(userid)
                .child(firebaseUser.getUid());

        chatReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    chatReference2.child("id").setValue(firebaseUser.getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recieveMessages(String currentuserId, String otherUserId){
        userChats = new ArrayList<>();
        DatabaseReference chatDatabaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        chatDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userChats.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    /** Gets ALL messages sent between you and the other chat participant. Adds to the userChats Collection **/
                    if(chat.getRecieverid().equals(currentuserId) && chat.getSenderid().equals(otherUserId) ||
                        chat.getRecieverid().equals(otherUserId) && chat.getSenderid().equals(currentuserId)){
                        userChats.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessagingActivity.this, userChats);
                    recyclerView.setAdapter(messageAdapter);
                    messageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
    CAUSES THE ACTIVITY TO CRASH

    private void setUserStatus(String userStatus){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userstatus", userStatus);
        reference.updateChildren(hashMap);
    }

   @Override
   protected void onResume() {
       super.onResume();
       setUserStatus("online");
       Log.w("status", "Setting the user status to online in the MessagingActivity");
   }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(messageSeenListener); // Remove the listener when the user exits the messaging activity
        setUserStatus("offline");
        Log.w("status", "Setting the user status to be offline from the MessagingActivity");
    }

    **/


    /** Set the message seen functionality - set it to true here **/
    /** This is the messaging activity. If the user has this activity opened and the message is retrieved then it is seen by them...and therefore 'messageseen' set to true **/
    private void seenMessage(String userid){
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        messageSeenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getRecieverid().equals(firebaseUser.getUid()) && chat.getSenderid().equals(userid)){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("messageseen",true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}