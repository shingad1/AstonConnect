package com.devin.astonconnect.Chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

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
    private ChatUserSearchAdapter chatUserSearchAdapter;
    private List<User> userList;
    private List<String> userIdList;
    private FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_chat_user_search, container, false);

        search_bar   = view.findViewById(R.id.search_bar);
        recyclerView = view.findViewById(R.id.recyclerView);
        userList     = new ArrayList<>();
        userIdList   = new ArrayList<>();

        //Firebase user
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        checkChats();
        readUsers();
        //setUpRecyclerView();

        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                chatUserSearchAdapter.getFilter().filter(text);
                //Toast.makeText(getActivity(), String.valueOf(text.length()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void setUpRecyclerView(){
        //recyclerview stuff
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatUserSearchAdapter = new ChatUserSearchAdapter(getContext(), userList);
        recyclerView.setAdapter(chatUserSearchAdapter);
    }

    //Get the list of the people that the user has already talked to
    private void checkChats(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    userIdList.add(dataSnapshot.getKey()); //the id of the other person you have a chat with
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readUsers(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    if(!user.getId().equals(firebaseUser.getUid())){
                        if(!userIdList.contains(user.getId())){
                            if(user.getisStaff() == false){
                                userList.add(user);
                            }
                        }
                    }
                }
                //Need to set up recyclerview here otherwise the list won't be added in full 
                setUpRecyclerView();
                chatUserSearchAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}