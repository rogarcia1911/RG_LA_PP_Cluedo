package com.example.rg_la_pp_cluedo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.example.rg_la_pp_cluedo.BBDD.Card;
import com.example.rg_la_pp_cluedo.BBDD.DataBaseConnection;
import com.example.rg_la_pp_cluedo.BBDD.Match;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.ObjectOutputStream;
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
        match = getMatch(MatchHelper.Mode.SOLO.name());

        // Comprobar si la última partida guardada ha terminado o no
        if (match.getEndingDate() != null)
        { // Si ha terminado creamos una nueva partida
            match = new Match();
            //match.setMatchId(Integer.valueOf(UUID.randomUUID().toString()));

            match.setBeginningDate(System.currentTimeMillis()); //Con un new Date convertimos los milisegundos a fecha
            match.setEndingDate(null);
            match.setMode(MatchHelper.Mode.SOLO.name());
            match.setDifficulty(MatchHelper.Difficulty.EASY.name());
            firebaseConnection.getFirebase(getContext()).child("Match").child(String.valueOf(match.getName())).setValue(match);

            generarCartasCulpables(match); // Guardamos las 3 cartas culpables

            jugar.putExtra("NewMatch",true);
        }
        jugar.putExtra("MatchName",match.getName());

        startActivity(jugar);
    }

    /**
     * Recupera la última partida SOLO o MULTI del usuario
     */
    private Match getMatch(String mode) {
        //TODO: select revision https://www.youtube.com/watch?v=_17qiNSMDCA&list=PL2LFsAM2rdnxv8bLBZrMtd_f3fsfgLzH7&index=5
        DatabaseReference db = firebaseConnection.getFirebase(getContext());
        Match match = null;

        DatabaseReference macthRef = db.child("Matchs").child(NAME_PLAYER+"Match");

        return match;
    }


    /**
     * Seleccionamos las 3 cartas del asesinato en la nueva partida
     * @param match Nueva partida donde se guardan las cartas
     */
    private void generarCartasCulpables(Match match) {
        //numero de cartas de cada tipo(N)
        int nCa = MatchHelper.Cards.D0.getNCardsByType();
        DatabaseReference murderCards = firebaseConnection.getFirebase(getContext()).child("Match").child(String.valueOf(match.getName())).child("MurderCards");
        // Asesino (10-15)
        murderCards.child( String.valueOf( 10 + ((int) (Math.random()*nCa)) ) );
        // Arma (20-25)
        murderCards.child( String.valueOf( 20 + ((int) (Math.random()*nCa)) ) );
        // Habitación (30-35)
        murderCards.child( String.valueOf( 30 + ((int) (Math.random()*nCa)) ) );

    }

    //Método del botón Iniciar partida Solo
    public void gameMulti(View view) {

    }
}