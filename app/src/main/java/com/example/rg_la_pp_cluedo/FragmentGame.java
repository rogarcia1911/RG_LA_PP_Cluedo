package com.example.rg_la_pp_cluedo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.rg_la_pp_cluedo.BBDD.DataBaseConnection;
import com.example.rg_la_pp_cluedo.BBDD.Match;
import com.example.rg_la_pp_cluedo.BBDD.User;
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

    SharedPreferences shSettings,shPreferences;
    FirebaseAuth mAuth;
    DatabaseReference database, userDataRef, matchDataRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        shSettings = this.getActivity().getSharedPreferences(getString(R.string.PREFsetttings), 0);
        shPreferences = this.getActivity().getSharedPreferences(getString(R.string.PREFapp),0);
        database = DataBaseConnection.getFirebase();
        mAuth = FirebaseAuth.getInstance();

        ibtRules = getView().findViewById(R.id.ibtRules);
        ibtSolo = getView().findViewById(R.id.ibtSolo);
        ibtMulti = getView().findViewById(R.id.ibtMulti);

        //TODO: preferancias idioma y sonido
        shSettings.getString("appLanguage","");
        shSettings.getBoolean("appSound",true);

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null && !shSettings.getString("userName","").isEmpty()){
            setupLogged();
            //TODO: Traducir textos
            Toast.makeText(getContext(), "Sesión ya iniciada", Toast.LENGTH_SHORT).show();
        } else {
            setupUnlogged();
            //TODO: Traducir textos
            Toast.makeText(getContext(), "No hay sesión iniciada", Toast.LENGTH_SHORT).show();
        }
        ibtRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                RulesFragment fragmentRules = new RulesFragment();
                transaction.replace(R.id.fragmentContainer, fragmentRules).commit();
            }
        });
    }

    private void setupLogged() {
        ibtSolo.setOnClickListener(v -> gameSolo(shSettings.getString("userName","")));

        ibtMulti.setOnClickListener(v -> gameMulti(shSettings.getString("userName","")));
    }

    private void setupUnlogged() {
        //TODO: Traducir textos
        ibtSolo.setOnClickListener(v -> Toast.makeText(getContext(), "Para jugar SOLO tienes que haber iniciado sesión.",Toast.LENGTH_SHORT).show());
        //TODO: Traducir textos
        ibtMulti.setOnClickListener(v -> Toast.makeText(getContext(), "Para jugar MULTI tienes que haber iniciado sesión.",Toast.LENGTH_SHORT).show());
    }

    private void addEventListener(Boolean newGame) {
        Context context = getContext();
        // Leer de la base de datos
        if (matchDataRef != null)
        matchDataRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Incia sesión - almacenasmo el usuario en el sharedPreferences
                Match match = dataSnapshot.getValue(Match.class);
                if(match != null && match.getEndingDate() == 0L){
                    SharedPreferences.Editor editor = shPreferences.edit();
                    if (match.getIsSolo()) {
                        editor.putString("gameSoloName", match.getName());
                        editor.putInt("gameSoloNum", match.getNum());
                        editor.putInt("gameSoloCont", MatchHelper.Difficulty.getContByName(match.getDifficulty()));
                    } else if (!match.getIsSolo()) {
                        editor.putString("roomName",match.getRoomName());
                        editor.putString("gameMultiName", match.getName());
                        editor.putInt("gameMultiNum", match.getNum());
                    }
                    editor.apply();

                    Toast.makeText(getContext(),"ActivityJuego", Toast.LENGTH_SHORT).show();
                    Intent jugar = new Intent( context, ActivityJuego.class);
                    jugar.putExtra("gameNew", newGame);
                    jugar.putExtra("gameMode",match.getIsSolo());
                    startActivity(jugar);

                } else {
                    SharedPreferences.Editor edit = shPreferences.edit();
                    edit.putString("gameSoloName", "");
                    edit.putInt("gameSoloNum", 0);
                    edit.putInt("gameSoloCont", 0);
                    edit.apply();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // ERROR al iniciar sesión
                // TODO : Texto para traducir
                Toast.makeText(getContext(),"Error al cargar la partida",Toast.LENGTH_SHORT).show();
            }
        });

        if(userDataRef!=null){
            DatabaseReference parent = userDataRef.getParent();
            parent.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Incia sesión - almacenamos el usuario en el sharedPreferences
                    User user = snapshot.getValue(User.class);
                    if(user != null){
                        SharedPreferences.Editor editor = shPreferences.edit();
                        editor.putString("userData", user.getEmail() + "\n" +
                                user.getPoints() + "\n" +
                                user.getNumSoloMatchs() + "\n" +
                                user.getNumMultiMatchs());
                        editor.apply();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    /**
     * Recupera la última partida SOLO o MULTI del usuario
     * @return lastMatch
     */
    private DatabaseReference getMatch(String userName, String mode, Integer num) {
        return database.getDatabase().getReference("Users/"+userName+"/Matchs/"+mode+"-"+num);
    }

    /**
     * Método del botón Play genera o recupera una partida SOLO y lleva al ActivityJuego
     * @param userName
     */
    public void gameSolo(String userName) {
        String gameSoloName = shPreferences.getString("gameSoloName", "");
        Integer gameSoloNum = shPreferences.getInt("gameSoloNum", 0);
        Integer gameSoloCont = shPreferences.getInt("gameSoloCont", 0);

        if (!gameSoloName.isEmpty() && gameSoloNum!=0 && gameSoloCont!=0)
            continueMatch(userName, gameSoloName);
        else {
            userDataRef = database.getDatabase().getReference("Users/"+userName+"/User/numSoloMatchs");
            userDataRef.get().addOnCompleteListener(task -> {
                Integer num = -1;
                num = task.getResult().getValue(num.getClass());

                if (num==0 ){
                    //TODO: en vez de newMatch tiene que ir abrir algo con 3 opciones para la DIFICULTAD
                    newMatch(userName, MatchHelper.Mode.SOLO.getB(),num+1);
                } else {
                    //Recuperar lastMatchRef con num match==Null =>new & match.Ending==Null =>new else continuePlaying
                    DatabaseReference lastMatchRef = getMatch(userName, MatchHelper.Mode.SOLO.name(),num);
                    Integer finalNum = num+1;
                    lastMatchRef.get().addOnCompleteListener(task1 -> {
                        // Comprobar si la última partida guardada ha terminado o no
                        //TODO: en vez de newMatch tiene que ir abrir algo con 3 opciones para la DIFICULTAD
                        Match match = task1.getResult().getValue(Match.class);
                        if (match != null && match.getEndingDate() == 0L)
                            continueMatch(userName,match.getName());
                        else
                            newMatch(userName,MatchHelper.Mode.SOLO.getB(), finalNum);
                    });

                }
            });
        }
    }

    /**
     * Métodod del botón Multiplayer que lleva al ActivityLobby
     * o recupera una partida MULTI y lleva al ActivityJuego
     * @param userName
     */
    public void gameMulti(String userName) {
        String roomName = shPreferences.getString("roomName","");
        String gameMultiName = shPreferences.getString("gameMultiName", "");
        Integer gameMultiNum = shPreferences.getInt("gameMultiNum", 0);

        if (!gameMultiName.isEmpty() && gameMultiNum!=0)
            continueMatch(userName, gameMultiName);
        else {
            Toast.makeText(getContext(),"ActivityLobby", Toast.LENGTH_SHORT).show();
            Intent lobby = new Intent(getContext(), ActivityLobby.class);
            startActivity(lobby);
        }
    }

    /**
     * Seleccionamos las 3 cartas del asesinato en la nueva partida
     */
    private ArrayList<Integer> generarCartasCulpables() {
        //numero de cartas de cada tipo(N)
        int nCa = MatchHelper.Cards.nCardsByType;
        ArrayList<Integer> murderCards = new ArrayList<Integer>();
        // Asesino (10-15)
        murderCards.add(10 + ((int) (Math.random() * nCa)) );
        // Arma (20-25)
        murderCards.add(20 + ((int) (Math.random() * nCa)) );
        // Habitación (30-35)
        murderCards.add(30 + ((int) (Math.random() * nCa)) );

        return murderCards;
    }

    private void newMatch(String userName, Boolean mode, Integer num) {
        Match match = new Match();
        match.setName(MatchHelper.Mode.getTextByB(mode) +"-"+ num);
        match.setNum(num);
        match.setBeginningDate(System.currentTimeMillis()); //Con un new Date convertimos los milisegundos a fecha
        match.setIsSolo(mode);
        if (mode) {
            match.setDifficulty(MatchHelper.Difficulty.EASY.name());
            match.setCont(MatchHelper.Difficulty.EASY.cont);
        }

        matchDataRef = database.getDatabase().getReference("Users/"+userName+"/Matchs/"+match.getName());
        match.setMurderCards(generarCartasCulpables());
        addEventListener(true);
        matchDataRef.setValue(match);
        userDataRef.setValue(num);

        Toast.makeText(getContext(),"Creamos: "+match.getName(), Toast.LENGTH_SHORT).show();
    }

    private void continueMatch(String userName, String gameName) {
        matchDataRef = database.getDatabase().getReference("Users/"+userName+"/Matchs/"+gameName);
        addEventListener(false);
        matchDataRef.get();
        //Toast.makeText(getContext(),"Continuamos: "+gameName, Toast.LENGTH_SHORT).show();
    }


}