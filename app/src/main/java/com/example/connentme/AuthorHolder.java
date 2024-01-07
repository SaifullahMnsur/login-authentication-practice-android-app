package com.example.connentme;
import com.google.firebase.auth.FirebaseAuth;
public class AuthorHolder {
    public static FirebaseAuth auth;

    // Optionally, you can add a method to initialize the FirebaseAuth instance
    static FirebaseAuth getAuth(){
        return auth;
    }

    public static FirebaseAuth Initialize() {
        auth = FirebaseAuth.getInstance();
        return  auth;
    }
}
