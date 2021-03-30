package com.devin.astonconnect.Journal;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.devin.astonconnect.Model.JournalItem;
import com.devin.astonconnect.R;
import com.google.android.material.slider.Slider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class JournalEntry1Fragment extends Fragment {
    private Spinner moodSpinner, moodLocationSpinner;
    private Slider moodIntensitySlider;;
    private EditText customMood, customMoodLocation, entryNameTv;
    private RelativeLayout selectDateButton;
    private RelativeLayout nextButton;
    private ImageView backBtn;
    //private TextView dateTimeText;

    //Custom spinner lists

    //mood
    private ArrayList<SpinnerItem> moodList;
    private SpinnerAdapter moodSpinnerAdapter;

    //locations
    private ArrayList<SpinnerItem> locationsList;
    private SpinnerAdapter moodLocationAdapter;


    //final set of values from user's input
    private String entryMood, entryLocation, entryIntensity, entryName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal_entry1, container, false);
        setUpLists();

        //Mood Spinner
        moodSpinner = view.findViewById(R.id.moodSpinner);
        moodSpinner.setAdapter(null);

        backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });


        customMood = view.findViewById(R.id.customMood);
        moodSpinner = view.findViewById(R.id.moodSpinner);
        moodSpinnerAdapter = new SpinnerAdapter(getActivity(), moodList);
        moodSpinner.setAdapter(moodSpinnerAdapter);
        moodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SpinnerItem selectedItem = (SpinnerItem) adapterView.getItemAtPosition(i);
                String selectedMood = selectedItem.getSpinnerItemName();

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



        customMoodLocation = view.findViewById(R.id.customMoodLocation);
        moodLocationSpinner = view.findViewById(R.id.moodLocationSpinner);
        moodLocationAdapter = new SpinnerAdapter(getActivity(), locationsList);
        moodLocationSpinner.setAdapter(moodLocationAdapter);
        moodLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                SpinnerItem item = (SpinnerItem) adapterView.getItemAtPosition(i);
                String selectedMoodLocation = item.getSpinnerItemName();

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
        moodIntensitySlider = view.findViewById(R.id.moodIntensitySlider);
        moodIntensitySlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                String selectedIntensity = Float.toString((int) value);
                if(selectedIntensity.equals("Select Intensity")){
                    entryIntensity = null;
                } else {
                    entryIntensity = selectedIntensity;
                    Toast.makeText(getActivity(), selectedIntensity, Toast.LENGTH_SHORT).show();
                }
            }
        });


        //Entry name
        entryNameTv = view.findViewById(R.id.entryName);
        entryNameTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                entryName = entryNameTv.getText().toString();
            }
        });


        /**Pass data to the next fragment...**/
        nextButton = view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((entryMood != null && entryLocation != null && entryIntensity != null  && entryName != null)){
                    JournalItem item = new JournalItem();
                    item.setEntryMood(entryMood);
                    item.setEntryLocation(entryLocation);
                    item.setEntryIntensity(entryIntensity);
                    item.setEntryName(entryName);

                    //Fragment fragment = new JournalEntry2Fragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("JournalItem", item);
                    //fragment.setArguments(bundle);

                    //navigate!
                    Navigation.findNavController(view).navigate(R.id.action_journalEntry1Fragment_to_journalEntry2Fragment, bundle);

                } else {
                    Toast.makeText(getActivity(), "Please ensure all fields are completed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void setUpLists(){
        moodList = new ArrayList<>();
        moodList.add(new SpinnerItem("Select Mood", R.drawable.ic_happy));
        moodList.add(new SpinnerItem("Anxious", R.drawable.ic_anxious));
        moodList.add(new SpinnerItem("Depressed", R.drawable.ic_depressed));
        moodList.add(new SpinnerItem("Angry", R.drawable.ic_angry));
        moodList.add(new SpinnerItem("Happy", R.drawable.ic_happy));
        moodList.add(new SpinnerItem("Relaxed", R.drawable.ic_relaxed));
        moodList.add(new SpinnerItem("Confused", R.drawable.ic_confused));
        moodList.add(new SpinnerItem("Other", R.drawable.ic_other));

        locationsList = new ArrayList<>();
        locationsList.add(new SpinnerItem("Select Location", R.drawable.ic_tap));
        locationsList.add(new SpinnerItem("Socialising", R.drawable.ic_chat2));
        locationsList.add(new SpinnerItem("At Home", R.drawable.ic_home_img));
        locationsList.add(new SpinnerItem("Work", R.drawable.ic_work));
        locationsList.add(new SpinnerItem("Shopping", R.drawable.ic_shopping_bag));
        locationsList.add(new SpinnerItem("Other", R.drawable.ic_tap));
    }


}