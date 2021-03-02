package com.devin.astonconnect.LoginRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.devin.astonconnect.MainActivity;
import com.devin.astonconnect.R;
import com.devin.astonconnect.Register.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    private Button register_btn;
    private Button login_btn;

    //Firebase Authentication and user
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        fAuth = FirebaseAuth.getInstance();

        register_btn = findViewById(R.id.register_btn);
        login_btn    = findViewById(R.id.login_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });
    }


    /**
     * If the user is already logged in(FirebaseAuth is not null), then start the main activity.
     */
    @Override
    protected void onStart() {
        super.onStart();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if(fUser != null){
            startActivity(new Intent(StartActivity.this, MainActivity.class));
            finish();
        }
    }
}