package com.example.connentme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.connentme.databinding.ActivityNewRegisterBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class NewRegisterActivity extends AppCompatActivity {
    FirebaseAuth auth = null;
    private final int RC_SIGN_IN = 13;
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

        // Google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.clienID)).requestEmail().build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);

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
        binding.btGoogleSignIn.setOnClickListener(view -> {
            googleSignInClient.signOut();
            startActivityForResult( googleSignInClient.getSignInIntent(), 13);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == RC_SIGN_IN && resultCode == RESULT_OK){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, handle the error
                Log.w("GoogleSignIn", "Google sign in failed at onActivityResult", e);
                Toast.makeText(NewRegisterActivity.this, e.getLocalizedMessage() + "Google Sign in failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                Toast.makeText(NewRegisterActivity.this, "Sign in successful", Toast.LENGTH_LONG).show();
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }else {
                // If sign in fails, display a message to the user.
                // You can log the exception message
                Log.w("FirebaseAuth", "signInWithCredential:failure", task.getException());
                // Show a Toast or handle the error in your UI
                Toast.makeText(NewRegisterActivity.this, "Authentication failed.", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(e ->
                Toast.makeText(NewRegisterActivity.this, e.getLocalizedMessage()+"on failure listener", Toast.LENGTH_LONG).show()
        );
    }
}