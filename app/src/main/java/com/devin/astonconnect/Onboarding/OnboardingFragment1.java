package com.devin.astonconnect.Onboarding;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.devin.astonconnect.R;
import com.devin.astonconnect.SharedPreferencesManager;


public class OnboardingFragment1 extends Fragment {

    private Button nextbutton;
    private SharedPreferencesManager manager;
    private Boolean isStaff;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_onboarding1, container, false);
        //get is staff
        manager = new SharedPreferencesManager(getActivity());
        isStaff = manager.getisStaff();
        Toast.makeText(getActivity(), "Is staff?: " + isStaff.toString(), Toast.LENGTH_SHORT).show();

        nextbutton = view.findViewById(R.id.nextButton);
        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_onboardingFragment1_to_onboardingFragment2);
            }
        });
        return view;
    }
}