package com.devin.astonconnect.Journal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.devin.astonconnect.Model.JournalItem;
import com.devin.astonconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class JournalEntry2Fragment extends Fragment {

    private EditText moodWhatHappened, moodThoughts;
    private Button submitBtn;
    private ImageView backBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_journal_entry2, container, false);

        //get object from previous fragment
        Bundle bundle = getArguments();
        JournalItem item = bundle.getParcelable("JournalItem");
        Toast.makeText(getActivity(), item.getEntryMood(), Toast.LENGTH_SHORT).show();

        moodWhatHappened = view.findViewById(R.id.moodWhatHappened);
        moodThoughts     = view.findViewById(R.id.moodThoughts);
        submitBtn = view.findViewById(R.id.submitBtn);

        backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(moodWhatHappened != null && moodThoughts != null) {
                    item.setEntryThoughts(moodThoughts.getText().toString());
                    item.setEntryWhatHappened(moodWhatHappened.getText().toString());

                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Journal").child(firebaseUser.getUid());
                    String entryId = databaseReference.push().getKey();

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("journalEntrySubmitted", String.valueOf(System.currentTimeMillis()));
                    hashMap.put("entryMood", item.getEntryMood());
                    hashMap.put("entryLocation", item.getEntryLocation());
                    hashMap.put("entryIntensity", item.getEntryIntensity());
                    hashMap.put("entryTime", item.getEntryTime());
                    hashMap.put("entryName", item.getEntryName());
                    hashMap.put("entryThoughts", item.getEntryThoughts());
                    hashMap.put("entryWhatHappened", item.getEntryWhatHappened());
                    hashMap.put("entryId", entryId);

                    databaseReference.child(entryId).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getActivity(), "Journal entry saved! " + ("\ud83d\ude01"), Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(view).navigate(R.id.action_journalEntry2Fragment_to_journalFragment);
                            }
                        }
                    });
                    /**
                    //Fragment fragment = new JournalEntry3Fragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("JournalItem", item);
                    //fragment.setArguments(bundle);

                    //navigate!
                    Navigation.findNavController(view).navigate(R.id.action_journalEntry2Fragment_to_journalEntry3Fragment, bundle);
                    **/
                } else {
                    Toast.makeText(getActivity(), "Please ensure all fields are completed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}