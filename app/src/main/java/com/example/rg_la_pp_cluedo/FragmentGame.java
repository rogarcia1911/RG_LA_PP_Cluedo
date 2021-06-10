package com.example.rg_la_pp_cluedo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.rg_la_pp_cluedo.BBDD.DataBaseConnection;
import com.example.rg_la_pp_cluedo.BBDD.Match;
import com.example.rg_la_pp_cluedo.BBDD.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class FragmentGame extends Fragment {

    private DataBaseConnection firebaseConnection;
    private List<Match> matchList = new ArrayList<>();
    private ArrayAdapter<Match> arrayAdapterMatch;
    private Match match;

    SharedPreferences shPreferences;
    FirebaseAuth mAuth;
    DatabaseReference database, userDataRef;
    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        shPreferences = this.getActivity().getSharedPreferences(getString(R.string.PREFapp),0);
        database = DataBaseConnection.getFirebase();
        mAuth = FirebaseAuth.getInstance();

        //TODO: preferancias idioma y sonido
        shPreferences.getString("appLanguage","");
        shPreferences.getBoolean("appSound",true);

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            //TODO: Traducir textos
            Toast.makeText(getContext(), "Sesión ya iniciada", Toast.LENGTH_SHORT).show();
        } else {

            //TODO: Traducir textos
            Toast.makeText(getContext(), "No hay sesión iniciada", Toast.LENGTH_SHORT).show();
        }
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
            match.setIsSolo(true);
            match.setDifficulty(MatchHelper.Difficulty.EASY.name());
            firebaseConnection.getFirebase().child("Match").child(String.valueOf(match.getName())).setValue(match);

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

        DatabaseReference macthRef = db.child("Matchs");

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