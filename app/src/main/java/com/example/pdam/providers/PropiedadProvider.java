package com.example.pdam.providers;

import android.util.Log;

import com.example.pdam.models.Propiedad;
import com.example.pdam.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PropiedadProvider {

    DatabaseReference mDatabaseReference;
    Propiedad propData;

    private static final String TAG = "DEV";

    /**
     * imicialización de la clase
     */
    public PropiedadProvider(){
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("propiedades");
        Log.i(TAG, "PropiedadProvider: getReference() " + mDatabaseReference);
    }

    /**
     * Guardar propiedad en la base de datos
     * @param propiedad
     * @return
     */
    public Task<Void> setPropiedad(Propiedad propiedad){
        /*
        recibimos id unico,
        rellenamos este campo en la propiedad
        guardamos propiedad en la BD
         */
        DatabaseReference pushedPropRef = mDatabaseReference.push();
        propiedad.setpID(pushedPropRef.getKey());
        return mDatabaseReference.child(propiedad.getpID()).setValue(propiedad);
    }

    /**
     * actualizar propiedad
     * @param prop
     * @return
     */
    public Task<Void> updatePropiedad(Propiedad prop) {
        Log.i(TAG, "PropieadProvider: inserción datos de usuario a BD " + prop);
        return mDatabaseReference.child(prop.getpID()).setValue(prop);
    }

    /**
     * obtener usuario de la base de datos por su identificador
     * @param pID
     * @return
     */
    public DatabaseReference getPropiedadById(String pID){
        return mDatabaseReference.child(pID);
    }

    /**
     * eliminar propiedad
     */

    public Task<Void> eliminarPropiedad(String pID){
        return mDatabaseReference.child(pID).removeValue();
    }

    public DatabaseReference getPropiedadesDataBaseReference(){
        return mDatabaseReference;
    }

}