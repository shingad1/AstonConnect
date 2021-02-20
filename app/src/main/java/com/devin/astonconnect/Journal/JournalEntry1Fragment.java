package com.devin.astonconnect.Journal;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.devin.astonconnect.R;

import java.util.ArrayList;


public class JournalEntry1Fragment extends Fragment {
    private Spinner moodSpinner, moodLocationSpinner, moodIntensitySpinner;
    private EditText customMood, customMoodLocation;
    private Button selectDateButton, selectTimeButton;

    //final set of values from user's input
    private String entryMood, entryLocation, entryIntensity, entryTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal_entry1, container, false);
        //custommood

        //Mood Spinner
        moodSpinner = view.findViewById(R.id.moodSpinner);
        customMood = view.findViewById(R.id.customMood);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.mood_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moodSpinner.setAdapter(adapter);
        moodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedMood = adapterView.getItemAtPosition(i).toString();

                if(selectedMood.equals("Other")){
                    customMood.setVisibility(View.VISIBLE);

                    customMood.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
                        @Override
                        public void afterTextChanged(Editable editable) {
                            entryMood = customMood.getText().toString();
                            Toast.makeText(getActivity(), entryMood, Toast.LENGTH_SHORT).show();
                        }
                    });


                } else {
                    customMood.setVisibility(View.GONE);
                    entryMood = selectedMood;
                }

                Toast.makeText(getActivity(), entryMood, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Mood Location
        moodLocationSpinner = view.findViewById(R.id.moodLocationSpinner);
        customMoodLocation = view.findViewById(R.id.customMoodLocation);
        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.mood_location, android.R.layout.simple_spinner_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moodLocationSpinner.setAdapter(locationAdapter);
        moodLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedMoodLocation = adapterView.getItemAtPosition(i).toString();

                if(selectedMoodLocation.equals("Other")){
                    customMoodLocation.setVisibility(View.VISIBLE);

                    customMoodLocation.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
                        @Override
                        public void afterTextChanged(Editable editable) {
                            entryLocation = customMoodLocation.getText().toString();
                            Toast.makeText(getActivity(), entryLocation, Toast.LENGTH_SHORT).show();
                        }
                    });


                } else {
                    customMoodLocation.setVisibility(View.GONE);
                    entryLocation = selectedMoodLocation;
                }

                Toast.makeText(getActivity(), entryLocation, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Mood Intensity
        moodIntensitySpinner = view.findViewById(R.id.moodIntensitySpinner);
        ArrayAdapter<CharSequence> intensityAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.mood_intensity, android.R.layout.simple_spinner_item);
        intensityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moodIntensitySpinner.setAdapter(intensityAdapter);
        moodIntensitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedIntensity = adapterView.getItemAtPosition(i).toString();
                entryIntensity = selectedIntensity;
                Toast.makeText(getActivity(), entryIntensity, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    //Date time

}