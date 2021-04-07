package com.devin.astonconnect.LoginRegister;

import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import com.devin.astonconnect.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class LoginActivityTest {
    //Helps to launch the activity
    @Rule
    public ActivityTestRule<LoginActivity> mLoginActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    private LoginActivity mLoginActivity = null;
    private EditText email_text = null;
    private EditText password_text = null;
    private RelativeLayout login_btn = null;

    @Before
    public void setUp() throws Exception {
        mLoginActivity = mLoginActivityTestRule.getActivity();
        email_text = mLoginActivity.findViewById(R.id.email_text);
        password_text = mLoginActivity.findViewById(R.id.password_text);
    }

    @Test
    public void testLaunchOfMainActivityOnButtonClick(){
        login_btn = mLoginActivity.findViewById(R.id.login_btn);
        assertNotNull(login_btn);


        onView(withId(R.id.email_text)).perform(closeSoftKeyboard()).perform(typeText("devinshingadia@gmail.com"));
        onView(withId(R.id.password_text)).perform(closeSoftKeyboard()).perform(typeText("Password123"));

        onView(withId(R.id.login_btn)).perform(closeSoftKeyboard()).perform(click());

    }


    @After
    public void tearDown() throws Exception {
        mLoginActivity = null;
    }
}