package com.example.pdam.providers;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthProvider {

    private FirebaseAuth mAuth;
    private static final String TAG = "DEV";

    public AuthProvider() {
        mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "AuthProvider: inicialización de mAuth: " + mAuth);
    }

    /**
     * El método crea una cuenta nueva con correo electronico y clave.
     *
     * @param uMail
     * @param uPass
     * @return Task<AuthResult>
     *
     */
    public Task<AuthResult> crear(String uMail, String uPass) {
        Log.d(TAG, "AuthProvider: creación nuevo usuario con eMail: " + uMail);
        return mAuth.createUserWithEmailAndPassword(uMail, uPass);
    }

    /**
     * El método autentifica el usuario por su correo electronico y clave.
     *
     * @param uMail
     * @param uPass
     * @return Task<AuthResult>
     *
     */
    public Task<AuthResult> logearse(String uMail, String uPass) {
        Log.d(TAG, "AuthProvider: logeo de usuario con eMail: " + uMail);
        return mAuth.signInWithEmailAndPassword(uMail, uPass);
    }

    /**
     *  El método cierra la sesión del usuario
     */
    public void cerarSesion(){
        mAuth.signOut();
    }

    /**
     * EL método devuelve los datos del usuario logedo del módulo Authentication
     * @return
     */
    public FirebaseUser getUsuario(){
        return mAuth.getCurrentUser();
    }

}
