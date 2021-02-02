package com.devin.astonconnect.LoginRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.devin.astonconnect.MainActivity;
import com.devin.astonconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, fullname, email, password, confirm_password;
    private Button register_btn, login_btn;
    private Switch userTypeToggle;
    private Boolean isStaff;

    //Firebase stuff
    private FirebaseAuth fAuth;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        fullname = findViewById(R.id.fullname);
        email    = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);
        userTypeToggle   = findViewById(R.id.userTypeToggle);
        isStaff = false;

        //buttons
        register_btn = findViewById(R.id.register_btn);
        login_btn = findViewById(R.id.login_btn);

        fAuth = FirebaseAuth.getInstance();


        //login btn
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //switch controls to get the 'isStaff' value from the switch
        userTypeToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStaff = userTypeToggle.isChecked();
                Log.w("CS Staff?: ", String.valueOf(isStaff));
                System.out.println(isStaff);
            }
        });


        //register btn
        //Can add regex here to check if the user belongs to aston university
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_username = username.getText().toString();
                String str_fullname = fullname.getText().toString();
                String str_email    = email.getText().toString();
                String str_password = password.getText().toString();
                String str_password_confirm = confirm_password.getText().toString();

                if(TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_fullname)|| TextUtils.isEmpty(str_email) ||
                        TextUtils.isEmpty(str_password) || TextUtils.isEmpty(str_password_confirm)){
                    Toast.makeText(RegisterActivity.this, "All fields are required.", Toast.LENGTH_SHORT).show();
                } else if (str_password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password should have atleast 6 characters.", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.equals(str_password, str_password_confirm)) {
                    Toast.makeText(RegisterActivity.this, "Please make sure passwords match.", Toast.LENGTH_SHORT).show();
                } else {
                    register(str_username, str_fullname, str_email, str_password, isStaff);
                }
            }
        });

    }

    private void register(String username, String fullname, String email, String password, Boolean isStaff){
        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = fAuth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("email", email);
                            hashMap.put("username", username.toLowerCase());
                            hashMap.put("fullname", fullname);
                            hashMap.put("isStaff", isStaff);
                            hashMap.put("modules", "");
                            hashMap.put("bio", "");
                            hashMap.put("imageurl", "https://firebasestorage.googleapis.com/v0/b/astonconnect-8c8f6.appspot.com/o/placeholder.png?alt=media&token=4354b93d-b968-4eff-8dee-0b28be3e505b");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent (RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "You can't register with this email or password.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}