package com.example.rg_la_pp_cluedo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.rg_la_pp_cluedo.BBDD.Card;
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

import java.lang.reflect.Array;
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
        ibtSolo.setOnClickListener(v -> gameSolo(shPreferences.getString("userName","")));

        ibtMulti.setOnClickListener(v -> gameMulti(shPreferences.getString("userName","")));
    }

    private void setupUnlogged() {
        //TODO: Traducir textos
        ibtSolo.setOnClickListener(v -> Toast.makeText(getContext(), "Para jugar SOLO tienes que haber iniciado sesión.",Toast.LENGTH_SHORT).show());
        //TODO: Traducir textos
        ibtMulti.setOnClickListener(v -> Toast.makeText(getContext(), "Para jugar MULTI tienes que haber iniciado sesión.",Toast.LENGTH_SHORT).show());
    }

    private void addEventListener() {
        // Leer de la base de datos
        if (matchDataRef != null)
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
                        editor.putInt("gameSoloCont", MatchHelper.Difficulty.getContByName(match.getDifficulty()));
                    } else if (!match.getIsSolo()) {
                        editor.putString("gameMultiName", match.getName());
                        editor.putInt("gameMultiNum", match.getNum());
                    }
                    editor.apply();

                    Toast.makeText(getContext(),"ActivityJuego", Toast.LENGTH_SHORT).show();
                    Intent jugar = new Intent(getContext(), ActivityJuego.class);
                    startActivity(jugar);
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
                    if(!user.getName().equals("")){
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
                    newMatch(userName, MatchHelper.Mode.SOLO.name(),num+1);
                } else {
                    //Recuperar lastMatchRef con num match==Null =>new & match.Ending==Null =>new else continuePlaying
                    DatabaseReference lastMatchRef = getMatch(userName, MatchHelper.Mode.SOLO.name(),num);
                    Integer finalNum = num+1;
                    lastMatchRef.get().addOnCompleteListener(task1 -> {
                        // Comprobar si la última partida guardada ha terminado o no
                        Match match = task1.getResult().getValue(Match.class);
                        if (match == null && match.getEndingDate() == null)
                            newMatch(userName,MatchHelper.Mode.SOLO.name(), finalNum);
                        else if (match != null)
                            continueMatch(userName,match.getName());
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

    private void newMatch(String userName, String mode, Integer num) {
        Match match = new Match();
        match.setName(mode +"-"+ num);
        match.setNum(num);
        match.setBeginningDate(System.currentTimeMillis()); //Con un new Date convertimos los milisegundos a fecha
        if (mode.equals(MatchHelper.Mode.SOLO.name())) {
            match.setIsSolo(true);
            match.setDifficulty(MatchHelper.Difficulty.EASY.name());
        }
        matchDataRef = database.getDatabase().getReference("Users/"+userName+"/Matchs/"+match.getName());
        match.setMurderCards(generarCartasCulpables());
        addEventListener();
        matchDataRef.setValue(match);
        userDataRef.setValue(num);

        Toast.makeText(getContext(),"Creamos: "+match.getName(), Toast.LENGTH_SHORT).show();
    }

    private void continueMatch(String userName, String gameSoloName) {
        matchDataRef = database.getDatabase().getReference("Users/"+userName+"/Matchs/"+gameSoloName);
        addEventListener();
        matchDataRef.get();
        Toast.makeText(getContext(),"Continuamos: "+gameSoloName, Toast.LENGTH_SHORT).show();
    }


}