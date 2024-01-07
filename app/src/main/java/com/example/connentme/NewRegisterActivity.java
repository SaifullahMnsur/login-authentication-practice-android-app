package com.example.connentme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.connentme.databinding.ActivityNewRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class NewRegisterActivity extends AppCompatActivity {
    FirebaseAuth auth = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNewRegisterBinding binding = ActivityNewRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
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
            String name = Objects.requireNonNull(binding.etNameRegister.getText()).toString();
            String phone = Objects.requireNonNull(binding.etPhoneNoRegister.getText()).toString();
            Log.d("com.example.connentme", email+' '+password);
            if( !email.isEmpty() && !password.isEmpty() ){
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener( this, task -> {
                    if( task.isSuccessful()){
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                        Objects.requireNonNull(auth.getCurrentUser()).updateProfile(profileUpdates);
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid());
                        userRef.child("phoneNumber").setValue(phone).addOnCompleteListener(task1 -> {
                            if (!task1.isSuccessful()){
                                Toast.makeText(NewRegisterActivity.this, "Found Error adding phone number!", Toast.LENGTH_LONG).show();
                            }
                        });
                        Toast.makeText(NewRegisterActivity.this, "Account created!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(NewRegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(this, e -> Toast.makeText(NewRegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }
}