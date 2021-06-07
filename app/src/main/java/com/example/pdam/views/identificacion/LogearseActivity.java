package com.example.pdam.views.identificacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pdam.R;
import com.example.pdam.providers.AuthProvider;
import com.example.pdam.views.PPAActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogearseActivity extends AppCompatActivity {

    private EditText etEmailLogin, etUpassLogin;
    private Button btnLogearse;

    private AuthProvider mAuthProvider;
    private DatabaseReference mDatabase;

    private static final String TAG = "DEV";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logearse);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Login");

        Log.i(TAG, "LogearseActivity: init");

        etEmailLogin = (EditText) findViewById(R.id.etEmailLogin);
        etUpassLogin = (EditText) findViewById(R.id.etUpassLogin);

        btnLogearse = (Button) findViewById(R.id.btnLogearse);

        //mAuth = FirebaseAuth.getInstance();
        mAuthProvider = new AuthProvider();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnLogearse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logearse();
            }
        });
    }

    private void logearse() {

        String eMail = etEmailLogin.getText().toString();
        String uPass = etUpassLogin.getText().toString();

        if (!eMail.isEmpty() && !uPass.isEmpty()) {
            //mAuth.signInWithEmailAndPassword(eMail, uPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            mAuthProvider.logearse(eMail, uPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.i(TAG, "LogearseActivity: inicio de proceso de logeo");
                    if (task.isSuccessful()) {
                        Log.i(TAG, "LogearseActivity: logeo exitoso para: " + eMail);
                        Toast.makeText(LogearseActivity.this, "se ha logeado con exito", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LogearseActivity.this, PPAActivity.class);
                        Log.i(TAG, "LogearseActivity: rederect a PPAActivity con usuario logeado");
                        startActivity(intent);
                        finish();
                    } else {
                        Log.i(TAG, "LogearseActivity: logeo fallado para: " + eMail);
                        Toast.makeText(LogearseActivity.this, "los credenciales no son correctos", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            Log.i(TAG, "LogearseActivity: datos introducidos no son correctos: ");
            Toast.makeText(LogearseActivity.this, "todos los campos deben ser rellenos correctamente", Toast.LENGTH_SHORT).show();
        }
    }
}