import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthSingleton {
    private static FirebaseAuthSingleton instance;
    private final FirebaseAuth firebaseAuth;

    private FirebaseAuthSingleton() {
        // Initialize FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static synchronized FirebaseAuthSingleton getInstance() {
        if (instance == null) {
            instance = new FirebaseAuthSingleton();
        }
        return instance;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }
}
