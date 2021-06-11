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
import android.widget.Toast;

import com.example.rg_la_pp_cluedo.BBDD.DataBaseConnection;
import com.example.rg_la_pp_cluedo.BBDD.Match;
import com.example.rg_la_pp_cluedo.BBDD.Player;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityLobby extends AppCompatActivity {

    private Button button = null;
    private ListView listViewLobby = null;
    List<String> roomsList;
    private ArrayAdapter adapter;

    String playerName = null;
    String roomName = null;

    SharedPreferences shSettings,shPreferences;
    DatabaseReference roomRef = null,database = null;
    DatabaseReference rooms = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        shSettings = getSharedPreferences(getString(R.string.PREFsetttings), 0);
        shPreferences = getSharedPreferences(getString(R.string.PREFapp),0);
        database = DataBaseConnection.getFirebase();

        playerName = shSettings.getString("userName", "");
        roomName = playerName;

        listViewLobby = findViewById(R.id.listView_lobby);
        button = findViewById(R.id.buttonCreate);

        roomsList = new ArrayList<String>();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setText("Creating room");
                button.setEnabled(false);
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
    }

    Match match = null;
    private void addRoomEventListener(){
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                button.setText("");
                button.setEnabled(true);
                String status = null;
                if(dataSnapshot.getKey().equals("player1") && match==null) {
                    status = "Wait";
                    match = new Match();
                    match.setName("MULTI");
                    match.setBeginningDate(System.currentTimeMillis()); //Con un new Date convertimos los milisegundos a fecha
                    match.setIsSolo(false);
                    match.setMurderCards(generarCartasCulpables());
                    roomRef.getParent().child("match").setValue(match);
                    roomRef.getParent().child("status").setValue(status);
                }if(dataSnapshot.getKey().equals("player2")) {
                    status = "T1";
                    roomRef.getParent().child("status");
                    roomRef.setValue(status);
                }

                if (status!=null){
                    Intent intent = new Intent(getApplicationContext(), ActivityJuego.class);
                    intent.putExtra("roomName", roomName);
                    intent.putExtra("myTurn",dataSnapshot.getKey());
                    intent.putExtra("status", status);
                    intent.putExtra("gameMode",false);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                button.setText("");
                button.setEnabled(true);
                //Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
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

                    adapter = new ArrayAdapter<>(ActivityLobby.this, android.R.layout.simple_list_item_1, roomsList);
                    listViewLobby.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
}