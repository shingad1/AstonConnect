package com.devin.astonconnect.Onboarding;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.devin.astonconnect.R;
import com.devin.astonconnect.SharedPreferencesManager;


public class OnboardingFragment2 extends Fragment {

    private RelativeLayout nextbutton, backButton;
    private TextView text, subText, subText2;
    private Boolean isStaff;
    private SharedPreferencesManager manager;
    private LottieAnimationView lottieAnimation;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding2, container, false);

        //get is staff
        manager = new SharedPreferencesManager(getActivity());
        isStaff = manager.getisStaff();

        //Setting values
        text = view.findViewById(R.id.text);
        subText = view.findViewById(R.id.subText);
        subText2 = view.findViewById(R.id.subText2);
        lottieAnimation = view.findViewById(R.id.lottieAnimation);


        if(isStaff){
            lottieAnimation.setAnimation(R.raw.social_media);
            text.setText("Posts");
            subText.setText("create image/text posts to showcase updates from the cs department");
            subText2.setText("respond to student queries in posts, interact with their posts");
        } else {
            lottieAnimation.setAnimation(R.raw.yoga);
            text.setText("Wellbeing");
            subText.setText("manage your wellbeing more easily using the mood journal");
        }

        nextbutton = view.findViewById(R.id.nextButton);
        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_onboardingFragment2_to_onboardingFragment3);
            }
        });

        backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_onboardingFragment2_to_onboardingFragment1);
            }
        });


        return view;
    }
}