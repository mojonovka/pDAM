package com.example.pdam.views.propiedad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.example.pdam.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PropSeachAttr extends AppCompatActivity {

    private TextView tvPropSeachProvincia;
    private TextView tvPropSeachMunicipio;
    private TextView tvPropSeachPeriodo;

    private FloatingActionButton floatBTN;

    private Switch swMunicipio;
    private Switch swProvincia;
    private Switch swPeriodo;

    private static final String TAG = "DEV";

    /**
     * parametros de busqueda
     *
     */

    private String saProvincia;
    private String saMunicipio;
    private String saPeriodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prop_seach_attr);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        saProvincia = getIntent().getStringExtra("saProvincia");
        saMunicipio = getIntent().getStringExtra("saMunicipio");
        saPeriodo = getIntent().getStringExtra("saPeriodo");

        inicializarComponentes();
        setEventListeners();

    }

    private void inicializarComponentes() {
        tvPropSeachProvincia = findViewById(R.id.prop_seach_provincia);
        tvPropSeachMunicipio = findViewById(R.id.prop_seach_municipio);
        tvPropSeachPeriodo = findViewById(R.id.prop_seach_periodo);

        tvPropSeachProvincia.setEnabled(false);
        tvPropSeachMunicipio.setEnabled(false);
        tvPropSeachPeriodo.setEnabled(false);

        swProvincia = findViewById(R.id.seachSwithProvincia);
        swMunicipio = findViewById(R.id.seachSwithMunicipio);
        swPeriodo = findViewById(R.id.seachSwithPeriodo);


        floatBTN = findViewById(R.id.prop_seach_save);

        if (!saProvincia.isEmpty()){
            tvPropSeachProvincia.setText(saProvincia);
            tvPropSeachProvincia.setEnabled(true);
            swProvincia.setChecked(true);
        }
        if (!saMunicipio.isEmpty()){
            tvPropSeachMunicipio.setText(saMunicipio);
            tvPropSeachMunicipio.setEnabled(true);
            swMunicipio.setChecked(true);
        }
        if (!saPeriodo.isEmpty()){
            tvPropSeachPeriodo.setText(saPeriodo);
            tvPropSeachPeriodo.setEnabled(true);
            swPeriodo.setChecked(true);
        }
    }

    private void setEventListeners() {

        swProvincia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swProvincia.isChecked()){
                    tvPropSeachProvincia.setEnabled(true);
                } else {
                    saProvincia = "";
                    tvPropSeachProvincia.setText(saProvincia);
                    tvPropSeachProvincia.setEnabled(false);
                }
            }
        });

        swMunicipio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swMunicipio.isChecked()){
                    tvPropSeachMunicipio.setEnabled(true);
                } else {
                    saMunicipio = "";
                    tvPropSeachMunicipio.setText(saMunicipio);
                    tvPropSeachMunicipio.setEnabled(false);
                }
            }
        });

        swPeriodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swPeriodo.isChecked()){
                    tvPropSeachPeriodo.setEnabled(true);
                } else {
                    saPeriodo = "";
                    tvPropSeachPeriodo.setText(saPeriodo);
                    tvPropSeachPeriodo.setEnabled(true);
                }
            }
        });

        floatBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("saProvincia", tvPropSeachProvincia.getText().toString().trim());
                intent.putExtra("saMunicipio", tvPropSeachMunicipio.getText().toString().trim());
                intent.putExtra("saPeriodo", tvPropSeachPeriodo.getText().toString().trim());
                Log.i(TAG, "PropSeachAttr: saProvincia = " + saProvincia);
                Log.i(TAG, "PropSeachAttr: saMunicipio = " + saMunicipio);
                Log.i(TAG, "PropSeachAttr: saPeriodo = " + saPeriodo);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}