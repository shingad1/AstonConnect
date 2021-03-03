package com.devin.astonconnect.Register;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devin.astonconnect.R;


public class RegisterFragment4 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        View view = inflater.inflate(R.layout.fragment_register4, container, false);

        if(bundle.getStringArrayList("interest_list") != null){
            Toast.makeText(getActivity(), "I can access the arraylist", Toast.LENGTH_SHORT).show();
        }
        return view;
    }
}