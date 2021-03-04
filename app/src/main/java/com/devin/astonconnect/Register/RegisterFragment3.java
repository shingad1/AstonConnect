package com.devin.astonconnect.Register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.devin.astonconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;


public class RegisterFragment3 extends Fragment {

    private RelativeLayout nextButtonLayout;
    private EditText aboutMeText, customInterests;
    private Spinner interestsSpinner;
    private Chip chip;
    private RelativeLayout addBtn;
    private LinearLayout customInterestLayout;
    private ChipGroup chipGroup;
    private String userInterest;
    private ArrayList<String> interestsList = new ArrayList<>();
    private Bundle bundle;
    private NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_register3, container, false);

        bundle = getArguments();

        nextButtonLayout = view.findViewById(R.id.finishButtonLayout);
        interestsSpinner = view.findViewById(R.id.interestsSpinner);
        aboutMeText      = view.findViewById(R.id.aboutMe_text);
        customInterests  = view.findViewById(R.id.customInterestsEditText);
        customInterestLayout = view.findViewById(R.id.customInterestsLayout);
        addBtn           = view.findViewById(R.id.addBtn);
        chipGroup        = view.findViewById(R.id.chipGroup);
        navController    = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

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

                } else if (!selectedInterest.equals("Select Interest")){
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
                String str_aboutme = aboutMeText.getText().toString();
                if(TextUtils.isEmpty(str_aboutme)){
                    Toast.makeText(getActivity(), "Please add some text about yourself", Toast.LENGTH_SHORT).show();
                    register();
                } else if (chipGroup.getChildCount() != 0){
                    bundle.putString("about_me", str_aboutme);
                    bundle.putStringArrayList("interest_list", interestsList);
                    Navigation.findNavController(view).navigate(R.id.action_registerFragment3_to_registerFragment4, bundle);
                    register();
                } else {
                    bundle.putString("about_me", str_aboutme);
                    register();
                }
            }
        });
        return view;
    }


    public void addChip(){
        Chip chip_item = (Chip) LayoutInflater.from(getActivity()).inflate(R.layout.chip_item, null, false);
        chip_item.setText(userInterest);
        interestsList.add(userInterest);

        chip_item.setOnCloseIconClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                chipGroup.removeView(view);
                interestsList.remove(userInterest);
            }
        });

        chipGroup.addView(chip_item);

        customInterests.getText().clear();
        userInterest = null;
    }


    public void register(){
        String email = bundle.getString("email");
        String password = bundle.getString("password");
        String username = bundle.getString("username");
        String fullname = bundle.getString("fullname");
        Boolean isStaff = bundle.getBoolean("isStaff");
        ArrayList<String> interests = bundle.getStringArrayList("interest_list");
        String imageUrl = bundle.getString("imageUrl");
        String aboutMe  = aboutMeText.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            String userid = firebaseUser.getUid();

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                            //Set user data
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("email", email);
                            hashMap.put("username", username.toLowerCase());
                            hashMap.put("fullname", fullname);
                            hashMap.put("isStaff", isStaff);
                            hashMap.put("userstatus", "offline");
                            hashMap.put("customuserstatus", "");
                            hashMap.put("modules", "");
                            hashMap.put("bio", aboutMe);
                            hashMap.put("imageurl", imageUrl);

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        if(interests.isEmpty()){
                                            Navigation.findNavController(nextButtonLayout).navigate(R.id.action_registerFragment3_to_registerFragment4);
                                        } else {
                                            pushInterests(interests);
                                        }
                                       FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(getActivity(), "Please check your email for verification.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getActivity(), "An error has occured. Please try again later..", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        FirebaseAuth.getInstance().signOut();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), "This email is already in use. You can't register with it.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void pushInterests(ArrayList<String> interests){
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        String userid = fUser.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("interests");
        for(String item : interests){
            reference.push().setValue(item);
        }
        Navigation.findNavController(nextButtonLayout).navigate(R.id.action_registerFragment3_to_registerFragment4);
    }
}