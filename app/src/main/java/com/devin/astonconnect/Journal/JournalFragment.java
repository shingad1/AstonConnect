package com.devin.astonconnect.Journal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.devin.astonconnect.Adapter.JournalAdapter;
import com.devin.astonconnect.Model.JournalItem;
import com.devin.astonconnect.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JournalFragment extends Fragment {

    private FloatingActionButton floatingActionButton;

    //Firebase
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    //Recyclerview stuff
    private RecyclerView recyclerView;
    private List<JournalItem> journalItems;
    private JournalAdapter journalAdapter;

    //LinearLayoutt for no entries animation
    private LinearLayout noEntriesLayout;
    private LottieAnimationView searchingAnimation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_journal, container, false);

        //recyclerView
        recyclerView = view.findViewById(R.id.studentPostRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        journalItems = new ArrayList<>();
        journalAdapter = new JournalAdapter(getContext(), journalItems);
        recyclerView.setAdapter(journalAdapter);

        getJournalItems();

        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_journalFragment_to_journalEntry1Fragment);
            }
        });

        //Show 'no journal entries' animation
        noEntriesLayout = view.findViewById(R.id.noEntriesLayout);
        searchingAnimation = view.findViewById(R.id.searchingAnimation);

        return view;
    }

    private int getRandomAnimationFile() {
        Random random = new Random();
        int randomInt = random.nextInt(5);
        int id = R.raw.searching_3;

        if (randomInt == 0) {
            id = R.raw.searching_1;
        } else if (randomInt == 1) {
            id = R.raw.searching_2;

        } else if (randomInt == 2) {
            id = R.raw.searching_3;

        } else if (randomInt == 3) {
            id = R.raw.searching_4;

        } else if (randomInt == 4) {
            id = R.raw.searching_4;
        }
        return  id;
    }


    private void getJournalItems(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Journal").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    JournalItem item = snapshot.getValue(JournalItem.class);
                    journalItems.add(item);
                }
                journalAdapter.notifyDataSetChanged();

                if(journalAdapter.getItemCount() == 0){
                    Toast.makeText(getActivity(), "There are no entries...", Toast.LENGTH_SHORT).show();
                    noEntriesLayout.setVisibility(View.VISIBLE);
                    searchingAnimation.setAnimation(getRandomAnimationFile());
                    searchingAnimation.setSpeed(1);
                    searchingAnimation.playAnimation();

                } else {
                    noEntriesLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}