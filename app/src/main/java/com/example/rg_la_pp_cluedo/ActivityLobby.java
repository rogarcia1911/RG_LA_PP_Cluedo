package com.example.rg_la_pp_cluedo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rg_la_pp_cluedo.BBDD.ChatMessage;
import com.example.rg_la_pp_cluedo.BBDD.DataBaseConnection;
import com.example.rg_la_pp_cluedo.BBDD.Match;
import com.example.rg_la_pp_cluedo.BBDD.Player;
import com.example.rg_la_pp_cluedo.BBDD.Room;
import com.example.rg_la_pp_cluedo.BBDD.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityLobby extends AppCompatActivity {

    private Button btCreate = null;
    private ListView listViewLobby = null;
    List<String> roomsList;
    private ArrayAdapter<String> adapter;

    String playerName = null;
    String roomName = null;

    SharedPreferences shSettings,shPreferences;
    FirebaseAuth mAuth;
    DatabaseReference database, userDataRef, matchDataRef;

    DatabaseReference roomRef = null;
    DatabaseReference rooms = null;

    //TODO: OnBackPressed eliminar sala si solo hay un jugador
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        shSettings = getSharedPreferences(getString(R.string.PREFsetttings), 0);
        shPreferences = getSharedPreferences(getString(R.string.PREFapp),0);
        database = DataBaseConnection.getFirebase();

        playerName = shSettings.getString("userName", "");
        roomName = shPreferences.getString("roomName", "");

        listViewLobby = findViewById(R.id.listView_lobby);
        btCreate = findViewById(R.id.buttonCreate);

        listViewLobby.setEnabled(true);
        roomsList = new ArrayList<String>();

        //Ya estaba en una partida
        if (!roomName.equals("")) {
            roomRef = database.getDatabase().getReference("Rooms/" + roomName + (roomName == playerName ? "/player1" : "/player2"));
            addRoomEventListener();
            roomRef.get();
        }

        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btCreate.setText("Creating room");
                btCreate.setEnabled(false);
                roomName = playerName;
                roomRef = database.getDatabase().getReference("Rooms/" + roomName +  "/player1");
                addRoomEventListener();
                roomRef.setValue(playerName);
            }
        });

        listViewLobby.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                roomName = roomsList.get(position);
                roomRef = database.getDatabase().getReference("Rooms/" + roomName + "/player2");
                addRoomEventListener();
                roomRef.setValue(playerName);
            }
        });

        addRoomsEventListener();
        roomRef.get();
    }

    private void addRoomEventListener(){
        roomRef.getParent().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               ArrayList players = new ArrayList();
                Iterable<DataSnapshot> matchs = dataSnapshot.getChildren();
                for (DataSnapshot snapshot : matchs) {
                    players.add(snapshot.getKey());
                }

                if (players.size()<2) {
                   btCreate.setText("WaitAnotherPlayer");
                   btCreate.setEnabled(false);
                   listViewLobby.setEnabled(false);
                } else {
                    updateRoom(playerName, roomName);
                    btCreate.setText("");
                    btCreate.setEnabled(true);

                    userDataRef = database.getDatabase().getReference("Users/"+playerName+"/User/numMultiMatchs");
                    userDataRef.get().addOnCompleteListener(task -> {
                        Integer num = -1;
                        num = task.getResult().getValue(num.getClass());

                        if (num==0 ){
                            //TODO: en vez de newMatch tiene que ir abrir algo con 3 opciones para la DIFICULTAD
                            newMultiMatch(playerName, MatchHelper.Mode.SOLO.getB(),num+1);
                        } else {
                            //Recuperar lastMatchRef con num match==Null =>new & match.Ending==Null =>new else continuePlaying
                            DatabaseReference lastMatchRef = getMatch(playerName, MatchHelper.Mode.MULTI.name(),num);
                            Integer finalNum = num+1;
                            lastMatchRef.get().addOnCompleteListener(task1 -> {
                                // Comprobar si la última partida guardada ha terminado o no
                                //TODO: en vez de newMatch tiene que ir abrir algo con 3 opciones para la DIFICULTAD
                                Match match = task1.getResult().getValue(Match.class);
                                if (match != null && match.getEndingDate() == 0L)
                                    continueMultiMatch(playerName, match.getName());
                                else
                                    newMultiMatch(playerName, MatchHelper.Mode.MULTI.getB(), finalNum);
                            });

                        }

                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                btCreate.setText("");
                btCreate.setEnabled(true);
                //Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateRoom(String userName, String roomName) {
        Room room = new Room();
        room.setName(roomName);
        room.setTurn((int) (Math.random() * 2));
        room.setChat(new ArrayList<ChatMessage>());
        room.setCardsPicked(new ArrayList<Integer>());

        roomRef.getParent().setValue(room);
    }

    /**
     * Recupera la última partida SOLO o MULTI del usuario
     * @return lastMatch
     */
    private DatabaseReference getMatch(String userName, String mode, Integer num) {
        return database.getDatabase().getReference("Users/"+userName+"/Matchs/"+mode+"-"+num);
    }

    private void addRoomsEventListener(){
        roomRef = database.getDatabase().getReference("Rooms");
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomsList.clear();
                Iterable<DataSnapshot> rooms = dataSnapshot.getChildren();
                for(DataSnapshot snapshot: rooms){
                    roomsList.add(snapshot.getKey());
                    if (snapshot.getKey()==playerName)
                        listViewLobby.setEnabled(false);
                }
                adapter = new ArrayAdapter<String>(ActivityLobby.this, android.R.layout.simple_list_item_1, roomsList);
                listViewLobby.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void newMultiMatch(String userName, Boolean mode, Integer num) {
        Match match = new Match();
        match.setName(mode +"-"+ num);
        match.setNum(num);
        match.setBeginningDate(System.currentTimeMillis()); //Con un new Date convertimos los milisegundos a fecha
        if (mode) {
            match.setIsSolo(true);
            match.setDifficulty(MatchHelper.Difficulty.EASY.name());
        } if (!mode){
            match.setIsSolo(false);
            match.setRoomName(roomName);
            //TODO: método como el de generarCulpables match.setMineCards(generarOtrasCartas);
        }
        matchDataRef = database.getDatabase().getReference("Users/"+userName+"/Matchs/"+match.getName());
        match.setMurderCards(generarCartasCulpables());
        addEventListener(true);
        matchDataRef.setValue(match);
        userDataRef.setValue(num);

        Toast.makeText(this,"Creamos: "+match.getName(), Toast.LENGTH_SHORT).show();
    }

    private void continueMultiMatch(String userName, String gameMultiName) {
        matchDataRef = database.getDatabase().getReference("Users/"+userName+"/Matchs/"+gameMultiName);
        addEventListener(false);
        matchDataRef.get();
        Toast.makeText(this,"Continuamos: "+gameMultiName, Toast.LENGTH_SHORT).show();
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

    private void addEventListener(Boolean newGame) {
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
                            editor.putString("gameMultiName", match.getName());
                            editor.putInt("gameMultiNum", match.getNum());
                            editor.putString("roomName",roomName);
                        }
                        editor.apply();

                        Toast.makeText(ActivityLobby.this,"ActivityJuego", Toast.LENGTH_SHORT).show();
                        Intent jugar = new Intent(ActivityLobby.this, ActivityJuego.class);
                        jugar.putExtra("gameNew", newGame);
                        jugar.putExtra("gameMode",match.getIsSolo());
                        startActivity(jugar);

                    } else {
                        SharedPreferences.Editor edit = shPreferences.edit();
                        edit.putString("gameSoloName", "");
                        edit.putInt("gameSoloNum", 0);
                        edit.putInt("gameSoloCont", 0);
                        edit.putString("roomName","");
                        edit.putString("gameMultiName", "");
                        edit.putInt("gameMultiNum", 0);
                        edit.apply();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // ERROR al iniciar sesión
                    // TODO : Texto para traducir
                    Toast.makeText(ActivityLobby.this,"Error al cargar la partida",Toast.LENGTH_SHORT).show();
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

}