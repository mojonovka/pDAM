package com.example.pdam.providers;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthProvider {

    FirebaseAuth mAuth;
    private static final String TAG = "DEV";

    public AuthProvider() {
        mAuth = FirebaseAuth.getInstance();
        Log.i(TAG, "AuthProvider: inicialización de mAuth: " + mAuth);
    }

    public Task<AuthResult> crear(String uMail, String uPass) {
        Log.i(TAG, "AuthProvider: creación nuevo usuario con eMail: " + uMail);
        return mAuth.createUserWithEmailAndPassword(uMail, uPass);
    }

    public Task<AuthResult> logear(String uMail, String uPass) {
        Log.i(TAG, "AuthProvider: logeo de usuario con eMail: " + uMail);
        return mAuth.signInWithEmailAndPassword(uMail, uPass);
    }

}
