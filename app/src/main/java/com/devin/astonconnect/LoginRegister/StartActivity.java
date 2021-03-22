package com.devin.astonconnect.LoginRegister;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.devin.astonconnect.MainActivity;
import com.devin.astonconnect.R;
import com.devin.astonconnect.Register.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    private RelativeLayout register_btn;
    private RelativeLayout login_btn;

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
     * Shows a dialog box if the user presses back button
     */
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StartActivity.super.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
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