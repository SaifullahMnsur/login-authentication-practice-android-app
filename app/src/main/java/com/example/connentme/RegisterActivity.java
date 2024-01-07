package com.example.connentme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.connentme.databinding.ActivityRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth auth = AuthorHolder.auth;

        ActivityRegisterBinding binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if( actionBar != null ){
            actionBar.setTitle("Register");
        }
        binding.tvLogin.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
        binding.btCreateNewAccount.setOnClickListener(view -> {
            String email = Objects.requireNonNull(binding.etEmailRegister.getText()).toString();
            String password = Objects.requireNonNull(binding.etPasswordRegister.getText()).toString();
            Log.d("com.example.connentme", email+' '+password);
            if( !email.isEmpty() && !password.isEmpty() ){
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener( this, task -> {
                    if( task.isSuccessful()){
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(this, e -> Toast.makeText(RegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }
}