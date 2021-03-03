package com.devin.astonconnect.Register;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.devin.astonconnect.R;


public class RegisterFragment3 extends Fragment {

    private RelativeLayout nextButtonLayout;
    private EditText aboutMeText, customInterests;
    private Spinner interestsSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_register3, container, false);

        Bundle bundle = getArguments();

        nextButtonLayout = view.findViewById(R.id.nextButtonLayout);
        interestsSpinner = view.findViewById(R.id.interestsSpinner);
        aboutMeText      = view.findViewById(R.id.aboutMe_text);
        customInterests  = view.findViewById(R.id.customInterests);

        //Spinner selection
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.interests, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interestsSpinner.setAdapter(adapter);
        interestsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedInterest = adapterView.getItemAtPosition(i).toString();
                if(selectedInterest.equals("Other")){
                    customInterests.setVisibility(View.VISIBLE);
                } else {
                    customInterests.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        nextButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_registerFragment3_to_registerFragment4);
            }
        });

        return view;
    }
}