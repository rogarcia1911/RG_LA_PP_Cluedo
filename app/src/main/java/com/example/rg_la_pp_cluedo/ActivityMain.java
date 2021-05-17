package com.example.rg_la_pp_cluedo;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ActivityMain extends AppCompatActivity {

    private String fich = "cartas.dat";
    private List<Match> matchList = new ArrayList<>();
    DataBaseConection firebaseConection = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    //Método del botón Iniciar
    public void inicar(View view) {
        //TODO: insert revision https://www.youtube.com/watch?v=765aoufNc8c&list=PL2LFsAM2rdnxv8bLBZrMtd_f3fsfgLzH7&index=4
        Intent jugar = new Intent(this, ActivityJuego.class);

        firebaseConection = DataBaseConection.getInstance();

        Match currentMatch = new Match();
        currentMatch.setMatchId(Integer.valueOf(UUID.randomUUID().toString()));
        //TODO: primary key method revision
        //TODO: primary key is not valid

        currentMatch.setBeginningDate(System.currentTimeMillis()); //Con un new Date convertimos los milisegundos a fecha
        currentMatch.setEndingDate(null);
        currentMatch.setMode(null);
        currentMatch.setResultGame(null);
        currentMatch.setMatchCards(null);
        currentMatch.setPlayerNum(null);
        firebaseConection.getFirebase().child("Match").child(String.valueOf(currentMatch.getMatchId())).setValue(currentMatch);



        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(
                this, "administracion", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("SELECT MAX(id),tiempoTot,datetime() FROM partidas",null);
        fila.moveToFirst();

        int idPartida = fila.getInt(0);
        //Si se cumple una de las condiciones se crea una nueva partida
        if (fila.getInt(0)==0 || fila.getString(1)!= null) {
            idPartida++;

            ContentValues registro = new ContentValues();
            registro.put("id", idPartida);
            registro.put("inicio", fila.getString(2));
            db.insert("partidas",null, registro);

            generarCartas();
            jugar.putExtra("nuevaPartida",true);
        }
        startActivity(jugar);
    }



    private void generarCartas() {
        String fich = "cartas.dat";

        Carta per1 = new Carta(getString(R.string.tvPers1), R.drawable.pj_amapola,false); //0
        Carta per2 = new Carta(getString(R.string.tvPers2), R.drawable.pj_blanco,false); //1
        Carta per3 = new Carta(getString(R.string.tvPers3), R.drawable.pj_celeste,false); //2
        Carta per4 = new Carta(getString(R.string.tvPers4), R.drawable.pj_mora,false); //3
        Carta per5 = new Carta(getString(R.string.tvPers5), R.drawable.pj_prado,false); //4
        Carta per6 = new Carta(getString(R.string.tvPers6), R.drawable.pj_rubio,false); //5

        Carta arm1 = new Carta(getString(R.string.tvArma1), R.drawable.arma_candelabro,false); //6
        Carta arm2 = new Carta(getString(R.string.tvArma2), R.drawable.arma_cuerda,false); //7
        Carta arm3 = new Carta(getString(R.string.tvArma3), R.drawable.arma_herramienta,false); //8
        Carta arm4 = new Carta(getString(R.string.tvArma4), R.drawable.arma_pistola,false); //9
        Carta arm5 = new Carta(getString(R.string.tvArma5), R.drawable.arma_punial,false); //10
        Carta arm6 = new Carta(getString(R.string.tvArma6), R.drawable.arma_tuberia,false); //11

        Carta hab1 = new Carta(getString(R.string.tvHab1),R.drawable.lugar_banio,false); //12
        Carta hab2 = new Carta(getString(R.string.tvHab2),R.drawable.lugar_comedor,false); //13
        Carta hab3 = new Carta(getString(R.string.tvHab3),R.drawable.lugar_dormitorio,false); //14
        Carta hab4 = new Carta(getString(R.string.tvHab4),R.drawable.lugar_estudio,false); //15
        Carta hab5 = new Carta(getString(R.string.tvHab5),R.drawable.lugar_garaje,false); //16
        Carta hab6 = new Carta(getString(R.string.tvHab6),R.drawable.lugar_patio,false); //17

        Carta[] cartas = {per1,per2,per3,per4,per5,per6,arm1,arm2,arm3,arm4,arm5,arm6,hab1,hab2,hab3,hab4,hab5,hab6};

        //numero de cartas de cada tipo(N), M*N
        int nCa = 6,fCa=nCa-1;
        //Seleccinamos 1 arma, 1 personaje y 1 habitacion
        int v1 = (int) (Math.random()*nCa),v2 = (int) (Math.random()*fCa+nCa),v3 = (int) (Math.random()*fCa+(nCa*2));
        String ca = "";
        for (int i=0 ; i<cartas.length ; i++) {
            if (i==v1) cartas[i].setCulpable(true);
            if (i==v2) cartas[i].setCulpable(true);
            if (i==v3) cartas[i].setCulpable(true);
        }

        try {
            ObjectOutputStream oos = new ObjectOutputStream(openFileOutput(fich, Context.MODE_PRIVATE));
            oos.writeObject(cartas);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Método del botón Reglas
    public void reglas(View view) {
        Intent reglas = new Intent(this, ActivityReglas.class);
        startActivity(reglas);
    }

    //Método del botón Historial
    public void historial(View view) {
        Intent historial = new Intent(this, ActivityHistorial.class);
        startActivity(historial);
    }

}