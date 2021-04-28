package com.example.pdam.views.propiedad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.pdam.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PropSeachAttr extends AppCompatActivity {

    private TextView tvPropSeachProvincia;
    private TextView tvPropSeachMunicipio;
    private TextView tvPropSeachPeriodo;

    private FloatingActionButton floatBTN;

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

        floatBTN = findViewById(R.id.prop_seach_save);

        if (saProvincia != null){
            tvPropSeachProvincia.setText(saProvincia);
        }
        if (saMunicipio != null){
            tvPropSeachMunicipio.setText(saMunicipio);
        }
        if (saPeriodo != null){
            tvPropSeachPeriodo.setText(saPeriodo);
        }
    }

    private void setEventListeners() {

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