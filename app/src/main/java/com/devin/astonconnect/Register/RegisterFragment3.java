package com.devin.astonconnect.Register;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.devin.astonconnect.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;


public class RegisterFragment3 extends Fragment {

    private RelativeLayout nextButtonLayout;
    private EditText aboutMeText, customInterests;
    private Spinner interestsSpinner;
    private Chip chip;
    private RelativeLayout addBtn;
    private LinearLayout customInterestLayout;
    private ChipGroup chipGroup;
    private String userInterest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_register3, container, false);

        Bundle bundle = getArguments();

        nextButtonLayout = view.findViewById(R.id.nextButtonLayout);
        interestsSpinner = view.findViewById(R.id.interestsSpinner);
        aboutMeText      = view.findViewById(R.id.aboutMe_text);
        customInterests  = view.findViewById(R.id.customInterestsEditText);
        customInterestLayout = view.findViewById(R.id.customInterestsLayout);
        addBtn           = view.findViewById(R.id.addBtn);
        chipGroup        = view.findViewById(R.id.chipGroup);

        //Spinner selection
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.interests, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interestsSpinner.setAdapter(adapter);
        interestsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedInterest = adapterView.getItemAtPosition(i).toString();
                if(selectedInterest.equals("Other")){
                    customInterestLayout.setVisibility(View.VISIBLE);

                    //Get the user's text here
                    customInterests.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            userInterest = editable.toString();
                        }
                    });

                } else if (selectedInterest != "Select Interest"){
                    customInterestLayout.setVisibility(View.GONE);
                        userInterest = selectedInterest;
                        addChip();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userInterest != null){
                          addChip();
                    }
                }
        });


        nextButtonLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_registerFragment3_to_registerFragment4);
            }
        });

        return view;
    }


    public void addChip(){
        Chip chip_item = (Chip) LayoutInflater.from(getActivity()).inflate(R.layout.chip_item, null, false);
        chip_item.setText(userInterest);

        chip_item.setOnCloseIconClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                chipGroup.removeView(view);
            }
        });

        if(userInterest != "Select Interest"){
            chipGroup.addView(chip_item);
        }
        customInterests.getText().clear();
        userInterest = null;
    }

    /**
    public void addChip(){
        for(int j = 0; j < chipGroup.getChildCount(); j++) {
            String currentChipText = ((Chip) chipGroup.getChildAt(j)).getText().toString();
            if(userInterest != currentChipText){
                Chip chip_item = (Chip) LayoutInflater.from(getActivity()).inflate(R.layout.chip_item, null, false);
                chip_item.setText(userInterest);
                chip_item.setOnCloseIconClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chipGroup.removeView(view);
                    }
                });
                chipGroup.addView(chip_item);
            }
        }
    }
     **/
}