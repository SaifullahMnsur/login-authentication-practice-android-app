package com.example.connentme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.connentme.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    public FirebaseAuth auth = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        Log.d("MainActivity", "AuthHolder initialized");
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Your code here
        binding.btSignIn.setOnClickListener(view -> {
            if( auth.getCurrentUser() == null ) {
                Intent intentLogin = new Intent(this, LoginActivity.class);
                startActivity(intentLogin);
//            finish();//
            }else{
                Toast.makeText(MainActivity.this,"You are already logged in! Please logout first!", Toast.LENGTH_LONG).show();
            }
        });
        binding.btSingOut.setOnClickListener(view -> {
            if (auth.getCurrentUser() != null) {
                auth.signOut();
                Intent intentMain = new Intent(this, MainActivity.class);
                startActivity(intentMain);
                finish();
            } else {
                Toast.makeText(MainActivity.this, "You are not logged in!", Integer.parseInt("1000")).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.tvSignDetails.setText(updateData());
    }

    private String updateData() {
        FirebaseUser currUser = auth.getCurrentUser();
        if (currUser != null) {
            Log.d("MainActivity", "UpdateData: User Name ");
            return currUser.getDisplayName() + "\n" + currUser.getEmail();
        } else {
            Log.d("MainActivity", "UpdateData: User is null");
            return "Not Logged in";
        }
    }
}