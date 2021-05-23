package com.example.rg_la_pp_cluedo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.rg_la_pp_cluedo.BBDD.Card;
import com.example.rg_la_pp_cluedo.BBDD.DataBaseConnection;
import com.example.rg_la_pp_cluedo.BBDD.Match;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentGame#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentGame extends Fragment {

    private DataBaseConnection firebaseConnection;
    private List<Match> matchList = new ArrayList<>();
    private ArrayAdapter<Match> arrayAdapterMatch;
    private Match match;

    DatabaseReference user;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LOG_IN = "logIn";
    private static final String NAME_PLAYER = "namePlayer";

    // TODO: Rename and change types of parameters
    private Boolean logIn;
    private String namePlayer;

    public FragmentGame() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param logIn Señala si hay una sesión iniciada.
     * @param namePlayer Nombre del usuario con sesión inicida.
     * @return A new instance of fragment FragmentGame.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentGame newInstance(Boolean logIn , String namePlayer) {
        FragmentGame fragment = new FragmentGame();
        Bundle args = new Bundle();
        args.putBoolean(LOG_IN, logIn);
        args.putString(NAME_PLAYER, namePlayer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            logIn = getArguments().getBoolean(LOG_IN);
            namePlayer = getArguments().getString(NAME_PLAYER);
        }
        firebaseConnection = DataBaseConnection.getInstance();
        
        //user = firebaseConnection.getFirebase().child("Users").child(NAME_PLAYER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    /**
     * Lleva al fragment Rules
     * @param view
     */
    public void reglas(View view) {
        Intent reglas = new Intent(getContext(), ActivityReglas.class);
        startActivity(reglas);
    }

    /**
     * Método del botón Play genera una partida y lleva al ActivityJuego
     * @param view
     */
    public void gameSolo(View view) {
        //TODO: insert revision https://www.youtube.com/watch?v=765aoufNc8c&list=PL2LFsAM2rdnxv8bLBZrMtd_f3fsfgLzH7&index=4
        Intent jugar = new Intent(getContext(), ActivityJuego.class);

        firebaseConnection = DataBaseConnection.getInstance();

        // Recuperar última partida jugada
        match = getMatch(MatchHelper.mode.Solo.name());

        // Comprobar si la última partida guardada ha terminado o no
        if (match.getEndingDate() != null)
        { // Si ha terminado creamos una nueva partida
            match = new Match();
            //match.setMatchId(Integer.valueOf(UUID.randomUUID().toString()));
            //TODO: primary key method revision
            //TODO: primary key is not valid

            match.setBeginningDate(System.currentTimeMillis()); //Con un new Date convertimos los milisegundos a fecha
            match.setEndingDate(null);
            match.setMode(MatchHelper.mode.Solo.name());
            generarCartasCulpables(); // Guardamos las 3 cartas culpables
            match.setDifficulty(MatchHelper.difficulty.Easy.name());
            firebaseConnection.getFirebase(getContext()).child("Match").child(String.valueOf(match.getName())).setValue(match);
        }

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(
                getContext(), "administracion", null, 1);
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

            //generarCartas();
            jugar.putExtra("nuevaPartida",true);
        }
        startActivity(jugar);
    }

    /**
     * Recupera la última partida Solo o Multi del usuario
     */
    private Match getMatch(String mode) {
        //TODO: select revision https://www.youtube.com/watch?v=_17qiNSMDCA&list=PL2LFsAM2rdnxv8bLBZrMtd_f3fsfgLzH7&index=5
        DatabaseReference db = firebaseConnection.getFirebase(getContext());
        Match match = null;

        DatabaseReference macthRef = db.child("Matchs").child(NAME_PLAYER+"Match");

        return match;
    }

    private Card[] generarCartasCulpables() {
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
        Card[] MurderCards = new Card[3];
        for (int i=0 ; i<cartas.length ; i++) {
            if (i==v1) {
                cartas[i].setCulpable(true);
            }
            if (i==v2) cartas[i].setCulpable(true);
            if (i==v3) cartas[i].setCulpable(true);
        }

        try {
            ObjectOutputStream oos = new ObjectOutputStream(getContext().openFileOutput(fich, Context.MODE_PRIVATE));
            oos.writeObject(cartas);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return MurderCards;
    }

    //Método del botón Iniciar partida Solo
    public void gameMulti(View view) {

    }
}