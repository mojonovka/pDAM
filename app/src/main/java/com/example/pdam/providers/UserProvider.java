package com.example.pdam.providers;

import android.util.Log;

import com.example.pdam.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProvider {

    DatabaseReference mDatabaseReference;
    User userInf;

    private static final String TAG = "DEV";

    /**
     * imicialización de la clase
     */
    public UserProvider() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        Log.i(TAG, "UserProvider: getReference() " + mDatabaseReference);
    }

    /**
     * Guardar usuario en la base de datos
     * @param user
     * @return
     */
    public Task<Void> setUsuario(User user) {
        Log.i(TAG, "UserProvider: inserción datos de usuario a BD " + user);
        return mDatabaseReference.child(user.getuID()).setValue(user);
    }

    /**
     * obtener usuario de la base de datos por su identificador
     * @param uID
     * @return
     */
    public DatabaseReference getUsuarioById(String uID){

        //userInf = null;
        /*
        Log.i(TAG, "UserProvider: obtención de datos del usuario con uID  " + uID);

        mDatabaseReference.child(uID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i(TAG, "UserProvider: fallo al obtener datos del usuario  " + snapshot.getValue(User.class));
                //userInf =  new User(snapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i(TAG, "UserProvider: fallo al obtener datos del usuario  " + uID);
                //fallo al obtener datos del usuario
            }
        });
        */
        return mDatabaseReference.child(uID);
    }
}
