package com.example.pdam.providers;

import android.util.Log;

import com.example.pdam.models.Inmueble;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PropiedadProvider {

    DatabaseReference mDatabaseReference;
    private static final String TAG = "DEV";


    public PropiedadProvider(){
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("inmueble");
        Log.i(TAG, "PropiedadProvider: getReference() " + mDatabaseReference);
    }

    /**
     * actualizar propiedad
     * @param prop
     * @return
     */
    /* PropiedadLayout 325
    public Task<Void> updateInmb(Inmueble prop) {
        Log.i(TAG, "PropieadProvider: inserción datos de usuario a BD " + prop);
        return mDatabaseReference.child(prop.getInmbID()).setValue(prop);
    }

     */

    /**
     * obtener inmueble de la base de datos por su identificador
     * @param pID
     * @return
     */
    public DatabaseReference getInmbById(String pID){
        return mDatabaseReference.child(pID);
    }


    /**
     * El método que guarda los datos del inmueble en la base de datos
     * @param inmb
     * @return
     */
    public Task<Void> setInmb(Inmueble inmb){
        return mDatabaseReference.child(inmb.getInmbID()).setValue(inmb);
    }


    /**
     * Elemina el inmueble de la base de datos
     * @param inmbID
     * @return
     */
    public Task<Void> delInmb(String inmbID){
        return mDatabaseReference.child(inmbID).removeValue();
    }


    /**
     * Devuelbe la referencia al nodo inmueble
     * @return
     */
    public DatabaseReference getInmbDBReference(){
        return mDatabaseReference;
    }

}
