package com.example.rg_la_pp_cluedo;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.rg_la_pp_cluedo.BBDD.DataBaseConnection;
import com.example.rg_la_pp_cluedo.BBDD.Match;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class FragmentHistory extends Fragment {

    private TextView tvId, tvMode, tvTiempo, tvResultado;
    String userName;
    Long  matchTime;

    SharedPreferences shSettings,shPreferences;
    FirebaseAuth mAuth;
    DatabaseReference database, matchsRef;

    private ArrayList<Match> matchList = new ArrayList<Match>();

    public FragmentHistory() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        database = DataBaseConnection.getFirebase();
        mAuth = FirebaseAuth.getInstance();
        shSettings = this.getActivity().getSharedPreferences(getString(R.string.PREFsetttings), 0);
        userName = shSettings.getString("userName","");

        //TODO: preferancias idioma y sonido
        shSettings.getString("appLanguage","");
        shSettings.getBoolean("appSound",true);

        tvId = getView().findViewById(R.id.tvId);
        tvMode = getView().findViewById(R.id.tvMode);
        tvTiempo = getView().findViewById(R.id.tvTiempo);
        tvResultado = getView().findViewById(R.id.tvResultado);


        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null && !userName.isEmpty()){
            matchsRef = database.getDatabase().getReference("Users/"+userName+"/Matchs");
            ListFirebase();
        } else {
            ListSQL();
            //TODO: Traducir textos
            //Toast.makeText(getContext(), "No hay sesión iniciada", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    private void ListSQL() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this.getContext(), "administracion",null,1);
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

        } else Toast.makeText(this.getContext(),getString(R.string.msjNoPartidas),Toast.LENGTH_SHORT).show();

        db.close();
    }

    private void ListFirebase() {
        //DatabaseReference orderMatchs =
        matchsRef = database.getDatabase().getReference("Users/"+userName+"/Matchs");
        matchsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                matchList.clear();
                Iterable<DataSnapshot> matchs = dataSnapshot.getChildren();
                for (DataSnapshot snapshot : matchs) {
                    matchList.add(snapshot.getValue(Match.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al recuperar las partidas.",Toast.LENGTH_SHORT).show();
            }
        });
        matchsRef.limitToLast(15).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                matchList.clear();
                Iterable<DataSnapshot> matchs = task.getResult().getChildren();
                for (DataSnapshot snapshot : matchs) {
                    matchList.add(snapshot.getValue(Match.class));
                }
                list();
            }
        });

    }

    private void list() {
        if (matchList != null && !matchList.isEmpty()) {
            iniciar();
            int num = matchList.size();;
            for (Match matchObj : matchList) {
                Date totalTime = null;
                Long temp = null;
                if (matchObj.getEndingDate()!=0L)
                    totalTime = new Date(Long.parseLong(matchObj.getEndingDate() - matchObj.getBeginningDate()+""));

                //TODO: dejamos el id? solo 1 y multi 1
                //tvId.setText( ((String) tvId.getText()) + num + "\n" );

                tvMode.setText( ((String) tvMode.getText()) +
                        (matchObj.getIsSolo() ? MatchHelper.Mode.SOLO.name() : MatchHelper.Mode.MULTI.name()) + "\n" );

                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");
                tvTiempo.setText( ((String) tvTiempo.getText()) + ( (totalTime != null) ? format.format(totalTime) : getString(R.string.tvTTNull) ) + "\n" );

                if (matchObj.getResultGame()) // resultado al ganar
                    tvResultado.setText( ((String) tvResultado.getText()) + getString(R.string.tvResultadoGanar) + "\n" );
                else if (( matchObj.getResultGame()==null || !matchObj.getResultGame() ) && matchObj.getEndingDate()==null) //resultado cuando no se ha terminado la partida
                    tvResultado.setText( ((String) tvResultado.getText()) + "------" + "\n" );
                else //resultad al perder
                    tvResultado.setText( ((String) tvResultado.getText()) + getString(R.string.tvResultadoPerder) + "\n" );
            }
        } else Toast.makeText(this.getContext(),getString(R.string.msjNoPartidas),Toast.LENGTH_SHORT).show();
    }

    //Método para borrar todas las partidas
    public void iniciar() {
        tvId.setText("\n");
        tvMode.setText("\n");
        tvTiempo.setText("\n");
        tvResultado.setText("\n");
    }
}