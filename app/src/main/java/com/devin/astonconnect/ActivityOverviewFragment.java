package com.devin.astonconnect;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devin.astonconnect.Adapter.ActivityOverviewAdapter;
import com.devin.astonconnect.Model.ActivityItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivityOverviewFragment extends Fragment {

    private RecyclerView recyclerView;
    private ActivityOverviewAdapter activityOverviewAdapter;
    private List<ActivityItem> activityItemList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_overview, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        activityItemList = new ArrayList<>();
        activityOverviewAdapter = new ActivityOverviewAdapter(getContext(), activityItemList);
        recyclerView.setAdapter(activityOverviewAdapter);

        readActivityItems();

        return view;
    }

    private void readActivityItems(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                activityItemList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ActivityItem activityItem = snapshot.getValue(ActivityItem.class);
                    activityItemList.add(activityItem);
                }
                Collections.reverse(activityItemList);
                activityOverviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}