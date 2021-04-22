package com.example.rg_la_pp_cluedo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActivityReglas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reglas);
    }

    public void volverMenu(View view) {
        Intent menu = new Intent( this, ActivityMain.class);
        startActivity(menu);
    }
}