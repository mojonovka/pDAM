package com.example.pdam.providers;

import android.util.Log;

import com.example.pdam.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProvider {

    DatabaseReference mDatabaseReference;
    private static final String TAG = "DEV";

    /**
     * Inicialización de la clase
     */
    public UserProvider() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        Log.d(TAG, "UserProvider: getReference() " + mDatabaseReference);
    }

    /**
     * Guardar usuario en la base de datos
     * @param user
     * @return
     */
    public Task<Void> setUsuario(User user) {
        Log.d(TAG, "UserProvider: inserción datos de usuario a BD " + user);
        return mDatabaseReference.child(user.getuID()).setValue(user);
    }

    /**
     * Se obtiene el usuario de la base de datos por su identificador
     * @param uID
     * @return
     */
    public DatabaseReference getUsuarioById(String uID){
        return mDatabaseReference.child(uID);
    }
}
