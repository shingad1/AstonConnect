package com.devin.astonconnect.Journal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.devin.astonconnect.Model.JournalItem;
import com.devin.astonconnect.R;

import org.w3c.dom.Text;

public class JournalEntry4Fragment extends Fragment {

    private TextView entryOutlookChanged;
    private TextView moodText;
    private Spinner changedMoodIntensity;
    private Button submitButton;

    //Values to push to DB
    private String  entrychangedIntensity;
    private String  entryOutlookChangedText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_journal_entry4, container, false);

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

                if(entryOutlookChanged.getText().toString() == null){
                    entryOutlookChangedText = "Outlook not changed";
                }

                if(entrychangedIntensity != null && entryOutlookChangedText != null){
                    item.setOutlookChanged(entryOutlookChangedText);
                    item.setChangedEntryIntensity(entrychangedIntensity);

                    Toast.makeText(getActivity(), "Fields not null. Would submit now ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Please ensure all fields are completed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}