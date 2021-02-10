package com.devin.astonconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.devin.astonconnect.LoginRegister.StartActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    //Firebase Authentication and user
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this,  R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            String publisher = intent.getString("publisherid");
            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
            editor.putString("profileid", publisher);
        }
    }

    /** User Status **/
    private void setUserStatus(String userStatus){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(fAuth.getCurrentUser().getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userstatus", userStatus);
        reference.updateChildren(hashMap);
    }

    //When the user pauses the app set their status to be offline
    @Override
    protected void onPause() {
        super.onPause();
        setUserStatus("offline");
        Log.w("status", "Setting user status to offline in the MainActivity");
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUserStatus("online");
        Log.w("status", "Setting the user status to online in the MainActvity");
    }

    /**
     * Checks to see if the current user is logged in. If they are not, then it will prompt them to
     * either register or log in through the StartActivity
     */
    @Override
    protected void onStart() {
        super.onStart();

        if(fUser == null){
            sendToStart();
        }
    }

    /** Simply sends the user to the startActivity (state not logged in) **/
    private void sendToStart(){
        Intent intent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}