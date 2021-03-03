package com.devin.astonconnect.Register;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.devin.astonconnect.LoginRegister.StartActivity;
import com.devin.astonconnect.R;


public class RegisterFragment4 extends Fragment {

    private RelativeLayout finishButtonLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register4, container, false);

        finishButtonLayout = view.findViewById(R.id.finishButtonLayout);

        finishButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StartActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }
}