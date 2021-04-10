package com.devin.astonconnect.LoginRegister;

import android.app.Activity;
import android.app.Instrumentation;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.test.espresso.Espresso;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.devin.astonconnect.MainActivity;
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
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class LoginActivityTest {
    //Helps to launch the activity
    @Rule
    public ActivityTestRule<LoginActivity> mLoginActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    private LoginActivity mLoginActivity = null;
    private EditText email_text = null;
    private EditText password_text = null;
    private RelativeLayout login_btn = null;

    //Activity Monitor - this is to check if it has visited the next activity
    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);

    @Before
    public void setUp() throws Exception {
        mLoginActivity = mLoginActivityTestRule.getActivity();
        email_text = mLoginActivity.findViewById(R.id.email_text);
        password_text = mLoginActivity.findViewById(R.id.password_text);
    }

    @Test
    public void testLaunchOfMainActivityonLogin(){
        login_btn = mLoginActivity.findViewById(R.id.login_btn);
        assertNotNull(login_btn);

        //Actions to do. Have provided some test values for login
        onView(withId(R.id.email_text)).perform(closeSoftKeyboard()).perform(typeText("devinshingadia@gmail.com"));
        onView(withId(R.id.password_text)).perform(closeSoftKeyboard()).perform(typeText("Password123"));

        //Click
        onView(withId(R.id.login_btn)).perform(closeSoftKeyboard()).perform(click());

        //Check to see if the main activity is not null
        Activity mainActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);
        assertNotNull(mainActivity);
        mainActivity.finish();
    }


    @After
    public void tearDown() throws Exception {
        mLoginActivity = null;
    }
}