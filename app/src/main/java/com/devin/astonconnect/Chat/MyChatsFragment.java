package com.devin.astonconnect.Chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
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


public class MyChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatUserAdapter chatUserAdapter;
    private List<User> userlist;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private List<ChatList> userIdList; //List of chats (other people who you have a conversation with) -> used to populate userList

    //LinearLayout for no chats animation
    private LinearLayout noChatsLayout;
    private LottieAnimationView searchingAnimation;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_my_chats, container, false);

        recyclerView = view.findViewById(R.id.studentPostRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        noChatsLayout = view.findViewById(R.id.noChatsLayout);
        searchingAnimation = view.findViewById(R.id.searchingAnimation);

        /**
         * UserIdList holds the Id's of the users you have talked to, whereas userList holds the actual user objects to be then displayed using chatUserAdapter.
         */
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userlist = new ArrayList<>();
        userIdList = new ArrayList<>();


        /** This way of determining who you (the current user) has talked to, is more efficient compared to before.
         *  Before: Retrieve all chats, and iterate through them. If you are the sender, then add the reciever. If you are the reciever, then add the sender id.
         *
         *  Now:    When a new chat message is sent, add an entry to 'ChatList' from the sendMessage function in messagingactivity.
         *          This keeps track of who you have talked to by storing the userid of the other user in the chat.
         *          Now we can simply access that collection (chatlist -> userid -> 'id' (of other user) instead of iterating through all chats.
         *          More efficient method.
         */
        reference = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userIdList.clear(); //id's of the user's to display

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatList chatList = snapshot.getValue(ChatList.class);
                    userIdList.add(chatList);
                }
                getChatUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    public void getChatUsers(){
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userlist.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    for(ChatList chatList : userIdList){
                        if(user.getId().equals(chatList.getId())){
                            if(user.getisStaff() == false){
                                userlist.add(user);
                            }
                        }
                    }
                }
                chatUserAdapter = new ChatUserAdapter(getContext(), userlist);
                recyclerView.setAdapter(chatUserAdapter);
                chatUserAdapter.notifyDataSetChanged();

                if (chatUserAdapter.getItemCount() == 0){
                    noChatsLayout.setVisibility(View.VISIBLE);
                } else {
                    noChatsLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}