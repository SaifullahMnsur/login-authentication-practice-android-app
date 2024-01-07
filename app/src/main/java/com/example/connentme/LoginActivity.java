package com.example.connentme;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.connentme.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth auth = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        ActionBar actionBar = getSupportActionBar();
        if( actionBar != null ) {
            actionBar.setTitle("Login");
        }
        Intent intent = new Intent(this, NewRegisterActivity.class);
        binding.tvRegister.setOnClickListener(view -> {
            startActivity(intent);
            finish();
        });
        binding.btLogin.setOnClickListener(view -> {
            String email = Objects.requireNonNull(binding.etEmailLogin.getText()).toString();
            String password = Objects.requireNonNull(binding.etPasswordLogin.getText()).toString();
            if( !email.isEmpty() && !password.isEmpty() ){
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                    if( task.isSuccessful()){
                        Intent intentMain = new Intent(this, MainActivity.class);
                        startActivity(intentMain);
                        finish();
                    }
                }).addOnFailureListener(this, e -> Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }
}