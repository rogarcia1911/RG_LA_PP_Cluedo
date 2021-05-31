package com.example.rg_la_pp_cluedo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rg_la_pp_cluedo.BBDD.DataBaseConnection;
import com.example.rg_la_pp_cluedo.BBDD.Match;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityHistorial extends AppCompatActivity {

    private TextView tvId, tvTiempo, tvResultado;
    Long  matchTime;

    DataBaseConnection firebaseConnection = null;
    private List<Match> matchList = new ArrayList<>();
    private ArrayAdapter<Match> arrayAdapterMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        tvId = findViewById(R.id.tvId);
        tvTiempo = findViewById(R.id.tvTiempo);
        tvResultado = findViewById(R.id.tvResultado);

        iniciar();

        firebaseConnection = DataBaseConnection.getInstance();

        dataList();

        //TODO: linear layout data revision (add player number?) and show all data
        //TODO: insert revision
        if (!matchList.isEmpty() && matchList != null) {
            for (Match matchObj : matchList) {

                if (matchObj.getEndingDate()!=null)
                    matchTime = matchObj.getEndingDate() - matchObj.getBeginningDate();
                //Date totalTime = new Date(matchTime);
                //TODO: convert long to date
                //matchObj.getPlayerNum();

                tvId.setText( ((String) tvId.getText()) + matchObj.getNum() + "\n" );

                if (matchObj.getEndingDate()!=null)
                    tvTiempo.setText( ((String) tvTiempo.getText()) + matchTime + "\n" );
                else
                    tvTiempo.setText( ((String) tvTiempo.getText()) + getString(R.string.tvTTNull));

                if (matchObj.getResultGame()) // resultado al ganar
                    tvResultado.setText( ((String) tvResultado.getText()) + getString(R.string.tvResultadoGanar) + "\n" );
                else if (( matchObj.getResultGame()==null || !matchObj.getResultGame() ) && matchObj.getEndingDate()==null) //resultado cuando no se ha terminado la partida
                    tvResultado.setText( ((String) tvResultado.getText()) + "------" + "\n" );
                else //resultad al perder
                    tvResultado.setText( ((String) tvResultado.getText()) + getString(R.string.tvResultadoPerder) + "\n" );
            }
        } else Toast.makeText(this,getString(R.string.msjNoPartidas),Toast.LENGTH_SHORT).show();

        /*
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
        */
    }


    private void dataList() {
        //TODO: select revision https://www.youtube.com/watch?v=_17qiNSMDCA&list=PL2LFsAM2rdnxv8bLBZrMtd_f3fsfgLzH7&index=5


        firebaseConnection.getFirebase(getApplicationContext()).child("Match").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                matchList.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    Match matchView = objSnapshot.getValue(Match.class);

                    if (matchView != null)
                        matchList.add(matchView);


                    arrayAdapterMatch = new ArrayAdapter<>(ActivityHistorial.this, android.R.layout.activity_list_item, matchList);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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