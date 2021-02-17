package com.example.pdam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.pdam.includes.IncToolBar;

public class AuthMainActivity extends AppCompatActivity {

    private Button btnIdentifivarse;
    private Button btnRegistrarse;

    private static final String TAG = "DEV";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Log.i(TAG, "AuthMainActivity: init");

        //IncToolBar.show(this, "Autenticación", true);

        btnIdentifivarse = (Button)findViewById(R.id.btnIdentificarse);
        btnRegistrarse = (Button)findViewById(R.id.btnRegistrarse);

        btnIdentifivarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "AuthMainActivity: inicio de identifiación");
                Intent intent = new Intent(AuthMainActivity.this, LogearseActivity.class);
                Log.i(TAG, "AuthMainActivity: rederect a LogearseActivity");
                startActivity(intent);
            }
        });

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "AuthMainActivity: inicio de registro");
                Intent intent = new Intent(AuthMainActivity.this, RegistrarseActivity.class);
                Log.i(TAG, "AuthMainActivity: rederect a RegistrarseActivity");
                startActivity(intent);
            }
        });

    }
}