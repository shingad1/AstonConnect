package com.devin.astonconnect.LoginRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.devin.astonconnect.MainActivity;
import com.devin.astonconnect.Model.User;
import com.devin.astonconnect.R;
import com.devin.astonconnect.Register.RegisterActivity;
import com.devin.astonconnect.SharedPreferencesManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private RelativeLayout login;
    private TextView text_signup, reset_password_text;
    private LottieAnimationView emailTickAnimation, passwordTickAnimation;

    //Animation
    private Boolean isAnimationPlayed1, isAnimationPlayed2 = false;
    private Pattern pattern = Pattern.compile("^[A-Za-z0-9._%+-]+@aston.ac.uk");


    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //animations
        emailTickAnimation = findViewById(R.id.emailTickAnimation);
        passwordTickAnimation = findViewById(R.id.passwordTickAnimation);


        email    = findViewById(R.id.email_text);
        password = findViewById(R.id.password_text);
        login    = findViewById(R.id.login_btn);
        text_signup = findViewById(R.id.register_text);

        fAuth = FirebaseAuth.getInstance();

        text_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        reset_password_text = findViewById(R.id.reset_password_text);
        reset_password_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email = email.getText().toString();
                String str_password = password.getText().toString();

                if(TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password)){
                    Toast.makeText(LoginActivity.this, "All fields are required.", Toast.LENGTH_SHORT).show();
                } else {
                    fAuth.signInWithEmailAndPassword(str_email, str_password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        /** If the user's email is verified, then sign them in normally, direct them to the main activity **/
                                        if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {

                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                                                    .child(fAuth.getUid());

                                            reference.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    setSharedPreferences(snapshot.getValue(User.class));

                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }


                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        } else {
                                            /** If the user has not verified their email**/
                                            Toast.makeText(LoginActivity.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                                            FirebaseAuth.getInstance().signOut();
                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Details not found. Please try again later.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });


        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                Matcher matcher = pattern.matcher(editable.toString());
                if(matcher.matches()){
                    if(!isAnimationPlayed1) {
                        emailTickAnimation.setVisibility(View.VISIBLE);
                        emailTickAnimation.setSpeed(1f);
                        emailTickAnimation.playAnimation();
                        isAnimationPlayed1 = true;
                    }
                } else {
                    emailTickAnimation.setSpeed(-1);
                    emailTickAnimation.playAnimation();
                    isAnimationPlayed1 = false;
                    emailTickAnimation.setVisibility(View.GONE);
                }
            }
        });


        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() > 6){
                    if(!isAnimationPlayed2) {
                        passwordTickAnimation.setVisibility(View.VISIBLE);
                        passwordTickAnimation.setSpeed(1f);
                        passwordTickAnimation.playAnimation();
                        isAnimationPlayed2 = true;
                    }
                } else {
                    passwordTickAnimation.setSpeed(-1);
                    passwordTickAnimation.playAnimation();
                    isAnimationPlayed2 = false;
                    passwordTickAnimation.setVisibility(View.GONE);
                }
            }
        });

    }

    private void setSharedPreferences(User user){
        //Set shared preferences values so that they can be retrieved at any time
        SharedPreferencesManager manager = new SharedPreferencesManager(LoginActivity.this.getApplicationContext());
        manager.setId(user.getId());
        manager.setisStaff(user.getisStaff());
        manager.setUsername(user.getUsername());
        manager.setFullname(user.getFullname());
        manager.setImageurl(user.getImageurl());
        manager.setBio(user.getBio());
        manager.setModules(user.getModules());
        manager.setEmail(user.getEmail());
        manager.setUserstatus(user.getUserstatus());
        manager.setCustomuserstatus(user.getCustomuserstatus());
    }
}