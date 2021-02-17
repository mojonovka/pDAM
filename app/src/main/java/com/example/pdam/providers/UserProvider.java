package com.example.pdam.providers;

import android.util.Log;

import com.example.pdam.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UserProvider {

    DatabaseReference mDatabaseReference;

    private static final String TAG = "DEV";

    public UserProvider() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        Log.i(TAG, "UserProvider: getReference() " + mDatabaseReference);
    }

    public Task<Void> crear(User user) {
        Log.i(TAG, "UserProvider: inserción datos de usuario a BD " + user);
        Map<String, Object> mapUser = new HashMap<>();
        mapUser.put("uName", user.getuName());
        mapUser.put("uEmail", user.getuEmail());
        return mDatabaseReference.child(user.getuID()).setValue(mapUser);
    }

}
