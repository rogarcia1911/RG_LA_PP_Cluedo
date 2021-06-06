package com.example.rg_la_pp_cluedo;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.rg_la_pp_cluedo.BBDD.DataBaseConnection;
import com.example.rg_la_pp_cluedo.BBDD.Match;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ActivityMain extends AppCompatActivity {

    FragmentTransaction transaction;

    DataBaseConnection firebaseConnection = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseConnection = DataBaseConnection.getInstance();
        DataBaseConnection.getFirebase(getApplicationContext());
        FragmentGame fragmentGame = new FragmentGame();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer,fragmentGame).commit();

        //Si pulsa el boton Back saldrá un dialog preguntando si esta seguro de quiere salir de la aplicación
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_salir, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMain.this)
                       // .setView(view)
                        .setMessage(getString(R.string.msjSalir))
                        .setPositiveButton(R.string.btPstv, new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent salir = new Intent(Intent.ACTION_MAIN);
                                salir.addCategory(Intent.CATEGORY_HOME);
                                salir.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(salir);
                            }
                        })
                        .setNegativeButton(R.string.btNgtv, null);

                final AlertDialog dialog = builder.create();
                dialog.show();

            }
        };
        getOnBackPressedDispatcher().addCallback(callback);

    }

    public void changeFragment(View view) {
        transaction = getSupportFragmentManager().beginTransaction();
        switch (view.getId())
        {
            case R.id.btAccount:
                LoginFragment fragmentAccount = new LoginFragment();
                transaction.replace(R.id.fragmentContainer, fragmentAccount).commit();
                break;
            case R.id.btHistorial:
                FragmentHistory fragmentHistory = new FragmentHistory();
                transaction.replace(R.id.fragmentContainer, fragmentHistory).commit();
                break;
            case R.id.btPlay:
                FragmentGame fragmentGame = new FragmentGame();
                transaction.replace(R.id.fragmentContainer, fragmentGame).commit();
                break;
            case R.id.btStore:
                FragmentStore fragmentStore = new FragmentStore();
                transaction.replace(R.id.fragmentContainer, fragmentStore).commit();
                break;
            default:
                RulesFragment fragmentRules = new RulesFragment();
                transaction.replace(R.id.fragmentContainer, fragmentRules).commit();
                break;
        }
    }
}