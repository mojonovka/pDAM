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
import com.example.pdam.models.User;
import com.example.pdam.providers.AuthProvider;
import com.example.pdam.providers.UserProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrarseActivity extends AppCompatActivity {

    private AuthProvider mAuthProvider;
    private UserProvider mUserProvider;

    private Button btnCrearCuenta;
    private EditText etUName, etEmail, etUpass;

    private static final String TAG = "DEV";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Creación de una cuenta nueva");

        Log.i(TAG, "RegistrarseActivity: init");

        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();

        btnCrearCuenta = (Button) findViewById(R.id.btnCrearCuenta);

        etUName = (EditText) findViewById(R.id.etUname);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etUpass = (EditText) findViewById(R.id.etUpass);

        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearCuenta();
            }
        });

    }

    private void crearCuenta() {

        String uName = etUName.getText().toString();
        String uMail = etEmail.getText().toString();
        String uPass = etUpass.getText().toString();

        //validación de los datos de entrada
        if (!uName.isEmpty() && !uMail.isEmpty() && !uPass.isEmpty()) {
            Log.i(TAG, "RegistrarseActivity: datos introducidos son correctos");
            registrarCuenta(uName, uMail, uPass);
        } else {
            Log.w(TAG, "RegistrarseActivity: datos introducidos no son correctos");
            Toast.makeText(this, "todos los campos deben ser rellenos correctamente", Toast.LENGTH_SHORT).show();
        }

    }

    private void registrarCuenta(String uName, String uMail, String uPass) {

        Log.i(TAG, "RegistrarseActivity: inicio de registro de cuenta con los datos: " + uName + " " + uMail + " " + uPass);
        mAuthProvider.crear(uMail, uPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Log.i(TAG, "RegistrarseActivity: nuevo usuario con eMail: " + uMail + " se ha creado co exito");
                    String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Log.i(TAG, "RegistrarseActivity: obtención de uID de nuevo usuario: " + uID);
                    guardarUsuario(uID, uName, uMail);
                } else {
                    Log.e(TAG, "RegistrarseActivity: fallo al crear nuevo usuario con eMail: " + uMail);
                    Toast.makeText(RegistrarseActivity.this, "no se ha podido crear una nueva cuenta", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void guardarUsuario(String uID, String uName, String uMail) {
        User user = new User(uID, uName, uMail);
        mUserProvider.setUsuario(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i(TAG, "RegistrarseActivity: guardamos los datos en BD");
                if (task.isSuccessful()) {
                    Log.i(TAG, "RegistrarseActivity: los datos se han guardado con exito en la base da datos");
                    Toast.makeText(RegistrarseActivity.this, "el usuario se ha guardado con exito", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistrarseActivity.this, LogearseActivity.class);
                    Log.i(TAG, "RegistrarseActivity: rederect a LogearseActivity");
                    startActivity(intent);
                    finish();
                } else {
                    Log.i(TAG, "RegistrarseActivity: fallo al guardar los datos en la BD");
                    Toast.makeText(RegistrarseActivity.this, "fallo al guardar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}