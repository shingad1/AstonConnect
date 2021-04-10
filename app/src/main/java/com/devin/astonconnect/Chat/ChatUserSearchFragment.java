package com.devin.astonconnect.Chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

public class ChatUserSearchFragment extends Fragment {

    private EditText search_bar;
    private RecyclerView recyclerView;
    private List<User> userList;
    private ChatUserSearchAdapter chatUserSearchAdapter;
    private FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_chat_user_search, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        search_bar   = view.findViewById(R.id.search_bar);
        userList     = new ArrayList<>();
        chatUserSearchAdapter = new ChatUserSearchAdapter(getContext(), userList);

        //recyclerview stuff
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(chatUserSearchAdapter);

        //Firebase user
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        readUsers();

        return view;
    }

    private void readUsers(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    if(!user.getId().equals(firebaseUser.getUid())){
                        if(user.getisStaff() == false){
                            userList.add(user);
                        }
                    }
                }
                chatUserSearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}