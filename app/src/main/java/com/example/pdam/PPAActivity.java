package com.example.pdam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PPAActivity extends AppCompatActivity {

    private TextView tvUserName;
    private Button btnOpcion;
    private boolean isUsuarioIdentificado;

    private static final String TAG = "DEV";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppa);

        Log.i(TAG, "PPAActivity: init");

        isUsuarioIdentificado = isUsuarioIdentificado();

        if (isUsuarioIdentificado){
            /**
             * mostrar recursos de busqueda con opción de elegir
             */
        } else {
            /**
             * mostrar recursos de busqueda y boton para identificarse
             */
        }

        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvUserName.setText("");

        btnOpcion = (Button) findViewById(R.id.btnOpcion);
        btnOpcion.setText("IDENTIFICARSE");
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

    private boolean isUsuarioIdentificado() {
        boolean usuarioIdentificado = false;
        /**
         * comprobamos la identificación
         */

        return usuarioIdentificado;
    }
}