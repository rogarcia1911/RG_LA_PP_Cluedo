package com.example.rg_la_pp_cluedo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.rg_la_pp_cluedo.BBDD.DataBaseConnection;
import com.example.rg_la_pp_cluedo.BBDD.Match;
import com.example.rg_la_pp_cluedo.BBDD.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentGame extends Fragment {

    private DataBaseConnection firebaseConnection;
    private List<Match> matchList = new ArrayList<>();
    private ArrayAdapter<Match> arrayAdapterMatch;
    private Match match;
    private ImageButton ibtRules, ibtSolo, ibtMulti;

    SharedPreferences shPreferences;
    FirebaseAuth mAuth;
    DatabaseReference database, userDataRef, matchDataRef;
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

        ibtRules = getView().findViewById(R.id.ibtRules);
        ibtSolo = getView().findViewById(R.id.ibtSolo);
        ibtMulti = getView().findViewById(R.id.ibtMulti);

        //TODO: preferancias idioma y sonido
        shPreferences.getString("appLanguage","");
        shPreferences.getBoolean("appSound",true);

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null && !shPreferences.getString("userName","").isEmpty()){
            setupLogged();
            //TODO: Traducir textos
            Toast.makeText(getContext(), "Sesión ya iniciada", Toast.LENGTH_SHORT).show();
        } else {
            setupUnlogged();
            //TODO: Traducir textos
            Toast.makeText(getContext(), "No hay sesión iniciada", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupLogged() {
        ibtSolo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameSolo(shPreferences.getString("userName",""));
            }
        });

        ibtMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameMulti(shPreferences.getString("userName",""));
            }
        });
    }

    private void setupUnlogged() {
        //TODO: Traducir textos
        ibtSolo.setOnClickListener(v -> Toast.makeText(getContext(), "Para jugar SOLO tienes que haber iniciado sesión.",Toast.LENGTH_SHORT).show());
        //TODO: Traducir textos
        ibtMulti.setOnClickListener(v -> Toast.makeText(getContext(), "Para jugar MULTI tienes que estar logeado.",Toast.LENGTH_SHORT).show());
    }

    private void addEventListener() {
        // Leer de la base de datos
        matchDataRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Incia sesión - almacenasmo el usuario en el sharedPreferences
                Match match = dataSnapshot.getValue(Match.class);
                if(!match.getName().equals("")){
                    SharedPreferences.Editor editor = shPreferences.edit();
                    if (match.getIsSolo()) {
                        editor.putString("gameSoloName", match.getName());
                        editor.putInt("gameSoloNum", match.getNum());
                        editor.putInt("gameSoloDif", MatchHelper.Difficulty.getContByName(match.getDifficulty()));
                    } else if (!match.getIsSolo()) {
                        editor.putString("gameMultiName", match.getName());
                        editor.putInt("gameMultiNum", match.getNum());
                    }
                    editor.apply();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // ERROR al iniciar sesión
                // TODO : Texto para traducir
                Toast.makeText(getContext(),"Error al cargar la partida",Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Método del botón Play genera o recupera una partida SOLO y lleva al ActivityJuego
     * @param userName
     */
    public void gameSolo(String userName) {
        database.getDatabase().getReference("Users/"+userName+"/User/numSoloMatchs")
        .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Integer num = null;
                num = task.getResult().getValue(num.getClass());

                //Recuperar match con num match==Null =>new & match.Ending==Null =>new else continuePlaying

                if (num==0 ){
                    newMatch(userName, MatchHelper.Mode.SOLO.name(),num);
                } else {
                    // Recuperar última partida jugada
                    Match match = getMatch(MatchHelper.Mode.SOLO.name(),num);
                    // Comprobar si la última partida guardada ha terminado o no
                    if (match.getEndingDate() != null)
                        newMatch(userName,MatchHelper.Mode.SOLO.name(),num);
                    //else

                }
            }
        });
        //TODO: insert revision https://www.youtube.com/watch?v=765aoufNc8c&list=PL2LFsAM2rdnxv8bLBZrMtd_f3fsfgLzH7&index=4
        Intent jugar = new Intent(getContext(), ActivityJuego.class);


        firebaseConnection = DataBaseConnection.getInstance();


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

            generarCartasCulpables(); // Guardamos las 3 cartas culpables

            jugar.putExtra("NewMatch",true);
        }
        jugar.putExtra("MatchName",match.getName());

        startActivity(jugar);
    }

    /**
     * Métodod del botón Multiplayer que lleva al ActivityLobby
     * @param userName
     */
    //Método del botón Iniciar partida Solo
    public void gameMulti(String userName) {

    }

    /**
     * Seleccionamos las 3 cartas del asesinato en la nueva partida
     */
    private void generarCartasCulpables() {
        //numero de cartas de cada tipo(N)
        int nCa = MatchHelper.Cards.D0.getNCardsByType();
        DatabaseReference murderCards = matchDataRef.child("MurderCards");
        // Asesino (10-15)
        murderCards.child( String.valueOf( 10 + ((int) (Math.random()*nCa)) ) );
        // Arma (20-25)
        murderCards.child( String.valueOf( 20 + ((int) (Math.random()*nCa)) ) );
        // Habitación (30-35)
        murderCards.child( String.valueOf( 30 + ((int) (Math.random()*nCa)) ) );

    }

    /**
     * Recupera la última partida SOLO o MULTI del usuario
     */
    private Match getMatch(String mode, Integer num) {
        //TODO: select revision https://www.youtube.com/watch?v=_17qiNSMDCA&list=PL2LFsAM2rdnxv8bLBZrMtd_f3fsfgLzH7&index=5
        DatabaseReference db = firebaseConnection.getFirebase(getContext());
        Match match = null;

        DatabaseReference macthRef = db.child("Matchs");

        return match;
    }

    private void newMatch(String userName, String mode, Integer num) {
        Match match = new Match();
        match.setName(mode + num++);
        match.setBeginningDate(System.currentTimeMillis()); //Con un new Date convertimos los milisegundos a fecha
        match.setEndingDate(null);
        if (mode.equals(MatchHelper.Mode.SOLO.name())) {
            match.setIsSolo(true);
            match.setDifficulty(MatchHelper.Difficulty.EASY.name());
        }
        matchDataRef = database.getDatabase().getReference("Users/"+userName+"/Matchs/"+match.getName());
        addEventListener();
        matchDataRef.setValue(match);

    }


}