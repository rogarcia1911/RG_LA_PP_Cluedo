package com.example.rg_la_pp_cluedo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ActivityHistorial extends AppCompatActivity {

    TextView tvId, tvTiempo, tvResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        tvId = findViewById(R.id.tvId);
        tvTiempo = findViewById(R.id.tvTiempo);
        tvResultado = findViewById(R.id.tvResultado);

        iniciar();

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion",null,1);
        SQLiteDatabase db = admin.getWritableDatabase();

        Cursor fila = db.rawQuery("SELECT id, IFNULL(tiempoTot,'"+getString(R.string.tvTTNull)+"'), resultado, fin FROM partidas ORDER BY id DESC LIMIT 20",null);
        if(fila.moveToFirst()){
            do {
                tvId.setText( ((String) tvId.getText()) + fila.getString(0) + "\n" );
                tvTiempo.setText( ((String) tvTiempo.getText()) + fila.getString(1) + "\n" );
                if (fila.getInt(2)==1) //resultado=true
                    tvResultado.setText( ((String) tvResultado.getText()) + getString(R.string.tvResultadoGanar) + "\n" );
                else if (fila.getInt(2)==0 && fila.getString(3)==null) //resultado=false y fin==null
                    tvResultado.setText( ((String) tvResultado.getText()) + "------" + "\n" );
                else //resultado=false y fin!=null
                    tvResultado.setText( ((String) tvResultado.getText()) + getString(R.string.tvResultadoPerder) + "\n" );
            }while(fila.moveToNext());

        } else Toast.makeText(this,getString(R.string.msjNoPartidas),Toast.LENGTH_SHORT).show();

        db.close();
    }

    //Método para borrar todas las partidas
    public void iniciar() {
        tvId.setText("\n");
        tvTiempo.setText("\n");
        tvResultado.setText("\n");
    }

    public void volverMenu(View view) {
        Intent menu = new Intent( this, ActivityMain.class);
        startActivity(menu);
    }

}