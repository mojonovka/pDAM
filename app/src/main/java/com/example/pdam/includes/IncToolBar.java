package com.example.pdam.includes;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.pdam.R;

public class IncToolBar {

    public static void show(AppCompatActivity activity, String titulo, boolean showBackButton){

        Toolbar toolbar = activity.findViewById(R.id.incToolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(titulo);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(showBackButton);
    }

}
