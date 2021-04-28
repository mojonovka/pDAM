package com.example.pdam.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pdam.R;

public class PPAAbout extends AppCompatActivity {

    private Button btnFeedBack;
    private final String EMAIL = "appmail@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppa_about);

        btnFeedBack = findViewById(R.id.app_btn_FeedBack);
        btnFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + EMAIL));
                startActivity(intent);
            }
        });
    }
}