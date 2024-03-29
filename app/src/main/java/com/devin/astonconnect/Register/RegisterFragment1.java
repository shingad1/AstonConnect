package com.devin.astonconnect.Register;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.devin.astonconnect.LoginRegister.StartActivity;
import com.devin.astonconnect.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterFragment1 extends Fragment {

    private RelativeLayout nextButtonLayout;
    private EditText username_text;
    private EditText fullnameText;
    private EditText emailText;
    private EditText emailConfirmText;
    private SwitchMaterial csStaffSwitch;
    private Boolean isStaff;
    private ImageView backBtn;

    //Pattern checking
    private Pattern pattern = Pattern.compile("^[A-Za-z0-9._%+-]+@aston.ac.uk");
    //private Pattern pattern = Pattern.compile("^[A-Za-z0-9._%+-]+.+"); //FOR TESTING PURPOSES


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register1, container, false);

        //get values
        username_text = view.findViewById(R.id.username_text);
        fullnameText = view.findViewById(R.id.fullnameText);
        emailText = view.findViewById(R.id.emailText);
        emailConfirmText = view.findViewById(R.id.emailConfirmText);
        csStaffSwitch = view.findViewById(R.id.csStaffSwitch);
        isStaff = false;
        backBtn = view.findViewById(R.id.backBtn);

        csStaffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    isStaff = true;
                } else {
                    isStaff = false;
                }
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StartActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        nextButtonLayout = view.findViewById(R.id.nextButtonLayout);
        nextButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_username = username_text.getText().toString();
                String str_fullname = fullnameText.getText().toString();
                String str_email = emailText.getText().toString();
                String str_email_confirm = emailConfirmText.getText().toString();

                Matcher matcher1 = pattern.matcher(str_email);
                Matcher matcher2 = pattern.matcher(str_email_confirm);

                if (TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_fullname) || TextUtils.isEmpty(str_email)
                        || TextUtils.isEmpty(str_email_confirm)) {
                    Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (str_username.length() < 4) {
                    Toast.makeText(getActivity(), "Password length cannot be less than 4 characters", Toast.LENGTH_SHORT).show();
                } else if (str_fullname.length() < 3) {
                    Toast.makeText(getActivity(), "Fullname length cannot be less than 3 characters", Toast.LENGTH_SHORT).show();
                } else if (!matcher1.matches()) {
                    Toast.makeText(getActivity(), "Please enter an aston email", Toast.LENGTH_SHORT).show();
                } else if (!matcher2.matches()) {
                    Toast.makeText(getActivity(), "Please enter an aston email for 'confirmed mail", Toast.LENGTH_SHORT).show();
                } else if (!str_email.equals(str_email_confirm)) {
                    Toast.makeText(getActivity(), "Please make sure both emails are matching", Toast.LENGTH_SHORT).show();
                } else if (isStaff) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("cs_staff");

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String staff_mail = snapshot.getValue().toString();
                                Log.w("staff_mail", staff_mail);

                                //If their staff mail is verified
                                if (staff_mail.equals(str_email)) {
                                    Log.w("valid_match", "yourmail: " + str_email + "staff_mail: " + staff_mail);

                                    //this code could be potentially put into a method?
                                    Bundle bundle = new Bundle();
                                    bundle.putString("username", str_username);
                                    bundle.putString("fullname", str_fullname);
                                    bundle.putString("email", str_email);
                                    bundle.putBoolean("isStaff", isStaff);
                                    Navigation.findNavController(view).navigate(R.id.action_registerFragment1_to_registerFragment2, bundle);

                                } else {
                                    Toast.makeText(getActivity(), "Staff mail not found. Please check the mail or try again later.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("username", str_username);
                    bundle.putString("fullname", str_fullname);
                    bundle.putString("email", str_email);
                    bundle.putBoolean("isStaff", isStaff);
                    //Pass to the next fragment
                    Navigation.findNavController(view).navigate(R.id.action_registerFragment1_to_registerFragment2, bundle);
                }
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_registerFragment1_to_startActivity);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);


        return view;
    }
}