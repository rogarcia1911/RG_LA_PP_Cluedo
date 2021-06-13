package com.example.rg_la_pp_cluedo;

import androidx.activity.OnBackPressedCallback;
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
import com.example.rg_la_pp_cluedo.BBDD.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
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

        database = iniFb();

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

    private void addRoomEventListener(){
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SharedPreferences shGameMulti = getSharedPreferences(getString(R.string.PREFmultiGame),0);
                if (dataSnapshot.getValue()==null || !dataSnapshot.exists())
                    return;

                button.setText(R.string.newSala);
                button.setEnabled(true);
                String status = null;
                if(dataSnapshot.getKey().equals("player1")) {
                    status = "player1:Wait";
                }if(dataSnapshot.getKey().equals("player2")) {
                    status = "player2:player1";
                }

                if (!shGameMulti.contains("roomName")){
                    SharedPreferences.Editor editor = shGameMulti.edit();
                    editor.putString("roomName", roomName);
                    editor.putString("myTurn",dataSnapshot.getKey());
                    editor.putString("status", status);
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(), ActivityJuego.class);
                    intent.putExtra("murderedCards", generarCartasCulpables());
                    intent.putExtra("gameNew",true);
                    intent.putExtra("gameMode",false);
                    startActivity(intent);
                } // else if !room.exist
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                button.setText(R.string.newSala);
                button.setEnabled(true);
                //Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addRoomsEventListener(){
        rooms = database.getDatabase().getReference("Rooms");
        rooms.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomsList.clear();
                if (!dataSnapshot.exists() || dataSnapshot==null) return;
                Iterable<DataSnapshot> rooms = dataSnapshot.getChildren();
                for(DataSnapshot snapshot: rooms){
                    if (snapshot.exists() && !snapshot.child("player2").exists())
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

    private DatabaseReference iniFb(){
        FirebaseApp.initializeApp(this);
        FirebaseDatabase firebaseObj = FirebaseDatabase.getInstance();
        if ( firebaseObj == null ) {
            firebaseObj.setPersistenceEnabled(true);
        }
        DatabaseReference databaseRefObj = firebaseObj.getReference();

        return databaseRefObj;
    }
}