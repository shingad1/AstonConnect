package com.devin.astonconnect.Chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.List;

//holds the active chats that the user can click on and open
public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatUserAdapter chatUserAdapter;
    private List<User> userlist;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private List<String> userIdList; //List of user id's (other people who you have a conversation with) -> used to populate userList


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userlist = new ArrayList<>();
        userIdList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userIdList.clear(); //id's of the users to display

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    //Add the reciever to the list if you are the sender (other person in conversation)
                    if(chat.getSenderid().equals(firebaseUser.getUid())){
                        userIdList.add(chat.getRecieverid());
                    }

                    //Add the sender to the list if you are the receiver (other person in conversation)
                    if(chat.getRecieverid().equals(firebaseUser.getUid())){
                        userIdList.add(chat.getSenderid());
                    }
                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    //Make sure to only add users who are non-staff members
    public void readChats(){
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userlist.clear();

                //Adding a user to be displayed. Staff cannot be shown.
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    for(String id : userIdList){
                        if (user.getId().equals(id)) {
                            if(user.getisStaff() == false){
                                if(userlist.size() != 0){
                                    for(User user1 : userlist){ //make sure they are not already listed
                                        if(!user.getId().equals(user1.getId())){
                                            userlist.add(user);
                                        }
                                    }
                                } else {
                                    userlist.add(user);
                                }
                            }
                        }
                    }
                }
                chatUserAdapter = new ChatUserAdapter(getContext(), userlist);
                recyclerView.setAdapter(chatUserAdapter);
                chatUserAdapter.notifyDataSetChanged(); //possibly remove?...
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}