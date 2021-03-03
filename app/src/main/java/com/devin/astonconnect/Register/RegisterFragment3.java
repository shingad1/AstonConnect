package com.devin.astonconnect.Register;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.devin.astonconnect.R;


public class RegisterFragment3 extends Fragment {

    private RelativeLayout nextButtonLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_register3, container, false);

        Bundle bundle = getArguments();
        Toast.makeText(getActivity(), bundle.getString("imageUrl"), Toast.LENGTH_SHORT).show();

        nextButtonLayout = view.findViewById(R.id.nextButtonLayout);
        nextButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_registerFragment3_to_registerFragment4);
            }
        });

        return view;
    }
}