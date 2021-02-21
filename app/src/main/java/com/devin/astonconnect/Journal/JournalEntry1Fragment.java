package com.devin.astonconnect.Journal;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.devin.astonconnect.Model.JournalItem;
import com.devin.astonconnect.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class JournalEntry1Fragment extends Fragment {
    private Spinner moodSpinner, moodLocationSpinner, moodIntensitySpinner;
    private EditText customMood, customMoodLocation;
    private Button selectDateButton;
    private Button nextButton;

    //final set of values from user's input
    private String entryMood, entryLocation, entryIntensity, entryTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal_entry1, container, false);

        //Mood Spinner
        moodSpinner = view.findViewById(R.id.moodSpinner);
        moodSpinner.setAdapter(null);


        customMood = view.findViewById(R.id.customMood);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.mood_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moodSpinner.setAdapter(adapter);
        moodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedMood = adapterView.getItemAtPosition(i).toString();

                if (selectedMood.equals("Select Mood")) {
                     entryMood = null;
                     customMood.setVisibility(View.GONE);

                } else if (selectedMood.equals("Other")){
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

        //Mood Location - don't know if this works..
        moodLocationSpinner = view.findViewById(R.id.moodLocationSpinner);
        moodLocationSpinner.setAdapter(null);


        customMoodLocation = view.findViewById(R.id.customMoodLocation);
        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.mood_location, android.R.layout.simple_spinner_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moodLocationSpinner.setAdapter(locationAdapter);
        moodLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedMoodLocation = adapterView.getItemAtPosition(i).toString();

                if (selectedMoodLocation.equals("Select Location")) {
                    entryLocation = null;
                    customMoodLocation.setVisibility(View.GONE);

                } else if (selectedMoodLocation.equals("Other")){

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

                if(selectedIntensity.equals("Select Intensity")){
                    entryIntensity = null;
                } else {
                    entryIntensity = selectedIntensity;
                    Toast.makeText(getActivity(), entryIntensity, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        selectDateButton = view.findViewById(R.id.selectDateButton);
        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                calendar.set(Calendar.MINUTE,minute);

                                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yy K:mm a");
                                Toast.makeText(getActivity(), simpleDateFormat.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
                                entryTime = simpleDateFormat.format(calendar.getTime());
                            }
                        };
                        new TimePickerDialog(getActivity(),timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
                    }
                };
                new DatePickerDialog(getActivity(),dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        /**Pass data to the next fragment...**/
        nextButton = view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((entryMood != null && entryLocation != null && entryIntensity != null && entryTime !=null)){
                    JournalItem item = new JournalItem();
                    item.setEntryMood(entryMood);
                    item.setEntryLocation(entryLocation);
                    item.setEntryIntensity(entryIntensity);
                    item.setEntryTime(entryTime);

                    Fragment fragment = new JournalEntry2Fragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("JournalItem", item);
                    fragment.setArguments(bundle);

                    //navigate!
                    Navigation.findNavController(view).navigate(R.id.action_journalEntry1Fragment_to_journalEntry2Fragment, bundle);

                } else {
                    Toast.makeText(getActivity(), "Please ensure all fields are completed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


}