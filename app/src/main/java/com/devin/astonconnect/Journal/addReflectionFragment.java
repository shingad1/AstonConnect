package com.devin.astonconnect.Journal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class addReflectionFragment extends Fragment {

    private TextView entryOutlookChanged;
    private TextView moodText;
    private Slider changedMoodIntensity;
    private Button submitButton;
    private TextView reflectedMoodText;

    //Values to push to DB
    private String entrychangedIntensity;
    private String entryOutlookChangedText;

    //Firebase
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    //back btn
    private ImageView backBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_reflection, container, false);

        //get object from previous fragment
        Bundle bundle = getArguments();
        JournalItem item = bundle.getParcelable("JournalItem");
        Toast.makeText(getActivity(), item.getEntryThoughts(), Toast.LENGTH_SHORT).show();

        //assign values
        entryOutlookChanged = view.findViewById(R.id.outlookChanged);
        moodText = view.findViewById(R.id.moodText);
        submitButton = view.findViewById(R.id.submitButton);
        reflectedMoodText = view.findViewById(R.id.reflectedMoodText);


        if (item.getEntrychangedIntensity() != null) {
            reflectedMoodText.setVisibility(View.VISIBLE);
            float originalIntensity = Float.parseFloat(item.getEntryIntensity());
            float changedIntensity = Float.parseFloat(item.getEntrychangedIntensity());

            if (originalIntensity > changedIntensity) {
                float difference = originalIntensity - changedIntensity;
                reflectedMoodText.setText("After reflecting, your mood decreased by " + String.valueOf(difference) + " strengths");
            }

            if (originalIntensity < changedIntensity) {
                float difference = changedIntensity - originalIntensity;
                reflectedMoodText.setText("After reflecting, your mood increased by " + String.valueOf(difference) + " strengths");
            }

            if (originalIntensity == changedIntensity) {
                reflectedMoodText.setText("After reflecting, your mood intensity stayed the same");
            }

        } else {
            reflectedMoodText.setVisibility(View.GONE);
        }


        if (item.getOutlookReflection() != null) {
            entryOutlookChanged.setText(item.getOutlookReflection());
            entryOutlookChangedText = item.getOutlookReflection();
        }


        entryOutlookChanged.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

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

        changedMoodIntensity = view.findViewById(R.id.changedMoodIntensity);
        changedMoodIntensity.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                String selectedIntensity = Float.toString(value);
                if (selectedIntensity.equals("Select Intensity")) {
                    entrychangedIntensity = null;
                } else if (selectedIntensity.equals("Mood not changed")) { //checkbox
                    entrychangedIntensity = item.getEntryIntensity(); //set to the older value
                } else {
                    entrychangedIntensity = selectedIntensity;
                    Toast.makeText(getActivity(), entrychangedIntensity, Toast.LENGTH_SHORT).show();
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (entrychangedIntensity != null && entryOutlookChangedText != null) {
                    item.setOutlookReflection(entryOutlookChangedText);
                    item.setEntrychangedIntensity(entrychangedIntensity);

                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Journal").child(firebaseUser.getUid()).child(item.getEntryId());

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("outlookReflection", item.getOutlookReflection());
                    hashMap.put("entrychangedIntensity", item.getEntrychangedIntensity());

                    databaseReference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
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
