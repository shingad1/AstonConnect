package com.devin.astonconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.devin.astonconnect.LoginRegister.StartActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    //Firebase Authentication and user
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private Button logoutBtn;
    private SharedPreferencesManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        manager = new SharedPreferencesManager(MainActivity.this.getApplicationContext());


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        //Programatically change the bottom navigation based on whether the user is staff or student
        if(manager.getisStaff() == true){
            bottomNavigationView.inflateMenu(R.menu.bottom_menu_staff);
        } else {
            bottomNavigationView.inflateMenu(R.menu.bottom_menu_student);
        }

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
       /**
        if(fAuth.getCurrentUser() != null){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(fAuth.getCurrentUser().getUid());
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("userstatus", userStatus);
            reference.updateChildren(hashMap);
        } else {
            Log.i("status", "fAuth is null. Cannot update user status");
        }
        **/
    }

    //When the user pauses the app set their status to be offline
    @Override
    protected void onPause() {
        super.onPause();
       // setUserStatus("offline");
        Log.w("status", "Setting user status to offline in the MainActivity");
    }


    @Override
    protected void onResume() {
        super.onResume();
       // setUserStatus("online");
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