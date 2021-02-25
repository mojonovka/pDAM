package com.example.pdam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pdam.models.User;
import com.example.pdam.providers.AuthProvider;
import com.google.firebase.auth.FirebaseAuth;

public class PPAActivity extends AppCompatActivity {

    private TextView tvUserName;
    private Button btnOpcion;
    private boolean isUsuarioIdentificado;
    private User usuario;
    private AuthProvider mAuthProvider;

    private static final String TAG = "DEV";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppa);

        Log.i(TAG, "PPAActivity: init");

        tvUserName = findViewById(R.id.tvUserName);

        btnOpcion = findViewById(R.id.btnOpcion);
        mAuthProvider = new AuthProvider();

        btnOpcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "PPAActivity: inicio de identifiación");
                Intent intent = new Intent(PPAActivity.this, AuthMainActivity.class);
                Log.i(TAG, "PPAActivity: rederect a AuthMainActivity");
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuthProvider.getUsuario() != null){
            isUsuarioIdentificado = true;
            /**
             * recibir datos del usuario
             */
            tvUserName.setText(FirebaseAuth.getInstance().getCurrentUser().getUid());
            btnOpcion.setText("cerar sesión");
        } else {
            isUsuarioIdentificado = false;
            tvUserName.setText("usuario anónimo");
            btnOpcion.setText("IDENTIFICARSE");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_cerrarsesion:
                mAuthProvider.cerarSecion();
                Intent intent = new Intent(PPAActivity.this, PPAActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}