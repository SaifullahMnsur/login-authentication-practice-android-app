package com.example.connentme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
        auth = AuthorHolder.Initialize();
        Log.d("MainActivity", "AuthHolder initialized");
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Your code here
        binding.btSignIn.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
        binding.btSingOut.setOnClickListener(view -> auth.signOut());

    }
        @Override
        protected void onResume() {
            super.onResume();
            binding.tvSignDetails.setText(updateData());
        }
        private String updateData(){
            FirebaseUser currAuth = auth.getCurrentUser();
            if( currAuth != null ){
                String email = currAuth.getEmail();
                Log.d("MainActivity", "UpdateData: User email - " + email);
                return "Email: " + email;
            }else{
                Log.d("MainActivity", "UpdateData: User is null");
                return "";
            }
        }
}
