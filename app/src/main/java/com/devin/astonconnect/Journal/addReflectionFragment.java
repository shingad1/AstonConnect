package com.devin.astonconnect.Journal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.devin.astonconnect.Model.JournalItem;
import com.devin.astonconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class addReflectionFragment extends Fragment {

    private TextView entryOutlookChanged;
    private TextView moodText;
    private Spinner changedMoodIntensity;
    private Button submitButton;

    //Values to push to DB
    private String  entrychangedIntensity;
    private String  entryOutlookChangedText;

    //Firebase
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    //back btn
    private ImageView backBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_add_reflection, container, false);

        //get object from previous fragment
        Bundle bundle = getArguments();
        JournalItem item = bundle.getParcelable("JournalItem");
        Toast.makeText(getActivity(), item.getEntryThoughts(), Toast.LENGTH_SHORT).show();

        //assign values
        entryOutlookChanged = view.findViewById(R.id.outlookChanged);
        moodText            = view.findViewById(R.id.moodText);
        submitButton        = view.findViewById(R.id.submitButton);

        entryOutlookChanged.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                entryOutlookChangedText = entryOutlookChanged.getText().toString();
            }
        });

        backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        //Set mood text
        moodText.setText("Your mood was: " + item.getEntryMood() + ", Strength " + item.getEntryIntensity());

        //Changed Mood intensity spinner
        changedMoodIntensity = view.findViewById(R.id.changedMoodIntensity);
        ArrayAdapter<CharSequence> intensityAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.changed_intensity, android.R.layout.simple_spinner_item);
        intensityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        changedMoodIntensity.setAdapter(intensityAdapter);
        changedMoodIntensity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedIntensity = adapterView.getItemAtPosition(i).toString();

                if(selectedIntensity.equals("Select Intensity")){
                    entrychangedIntensity = null;
                } else if (selectedIntensity.equals("Mood not changed")) {
                    entrychangedIntensity = item.getEntryIntensity(); //set to the older value
                } else {
                    entrychangedIntensity = selectedIntensity;
                    Toast.makeText(getActivity(), entrychangedIntensity, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(entrychangedIntensity != null && entryOutlookChangedText != null){
                    item.setOutlookReflection(entryOutlookChangedText);
                    item.setChangedEntryIntensity(entrychangedIntensity);

                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Journal").child(firebaseUser.getUid()).child(item.getEntryId());

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("outlookReflection", item.getOutlookReflection());
                    hashMap.put("entrychangedIntensity", item.getChangedEntryIntensity());

                    databaseReference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getActivity(), "Journal entry saved! " + ("\ud83d\ude01"), Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(view).navigate(R.id.action_addReflectionFragment_to_newsfeedFragment);
                            }
                        }
                    });

                } else {
                    Toast.makeText(getActivity(), "Please ensure all fields are completed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}