package com.example.pdam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvAPPName;
    private TextView tvLoader;
    private ProgressBar pbLoad;

    private Intent intent;

    private int millisInFuture = 2100;
    private int countDownInterval = 10;
    private int progress = 0;

    private static final String TAG = "DEV";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "inicio de APP");
        Log.i(TAG, "MainActivity: init");

        tvAPPName = (TextView) findViewById(R.id.tvAPPName);
        tvLoader = (TextView) findViewById(R.id.tvLoad);
        pbLoad = (ProgressBar) findViewById(R.id.pbLoad);
        intent = new Intent(MainActivity.this, PPAActivity.class);

        tvAPPName.setText("Proyecto DAM");

        Log.i(TAG, "MainActivity: inicio de carga de recursos");
        new CountDownTimer(millisInFuture, countDownInterval) {

            @Override
            public void onTick(long millisUntilFinished) {

                progress = (int) (100 - (100 * millisUntilFinished) / millisInFuture);
                tvAPPName.setText("Proyecto DAM\t");
                tvLoader.setText(progress + "%");
                pbLoad.setProgress(progress);

            }

            @Override
            public void onFinish() {
                pbLoad.setProgress(100, true);
                Log.i(TAG, "MainActivity: fin de carga de recursos");
                goToPPAActivity();
            }

        }.start();

    }

    private void goToPPAActivity() {
        Log.i(TAG, "MainActivity: rederect a PPAActivity");
        startActivity(intent);
    }

}