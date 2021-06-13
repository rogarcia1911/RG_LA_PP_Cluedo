package com.example.rg_la_pp_cluedo;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rg_la_pp_cluedo.BBDD.DataBaseConnection;
import com.example.rg_la_pp_cluedo.BBDD.Match;
import com.example.rg_la_pp_cluedo.BBDD.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
public class ActivityGanar extends AppCompatActivity {

    SharedPreferences shSettings;
    DataBaseConnection firebaseConnection = null;
    DatabaseReference database, userRef, roomRef;
    private MediaPlayer mp;

    private ImageView ivPers,ivArma,ivHab;

    private String fich = "cartas.dat";
    private SoundPool sp;
    private int sonido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganar);
        shSettings = getSharedPreferences(getString(R.string.PREFsetttings), 0);
        firebaseConnection = DataBaseConnection.getInstance();
        database = DataBaseConnection.getFirebase(getApplicationContext());

        //Si pulsa el boton Back le llevará al ActivityMain
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                menu();
            }
        };
        getOnBackPressedDispatcher().addCallback(callback);

        String userName = shSettings.getString("userName","");
        String roomName = (getIntent().hasExtra("roomName")) ? getIntent().getStringExtra("roomName") : null;
        Integer points=0;

        if (roomName!=null) {
            roomRef = database.getDatabase().getReference("Rooms/"+roomName);
            roomRef.removeValue();
            points = 15;
        } else
            points = 10;
        userRef = database.getDatabase().getReference("Users/"+userName+"/User");
        Integer finalPoints = points;
        userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                User user = task.getResult().getValue(User.class);
                user.setPoints(user.getPoints() + finalPoints);
                userRef.setValue(user);
            }
        });


        ArrayList<Integer> murderCards = getIntent().getExtras().getIntegerArrayList("murderCards");
        //TODO: mostrar las culpables
        ivPers = findViewById(R.id.ivPers);
        ivPers.setImageResource(MatchHelper.Cards.getImgByRef(murderCards.get(0)));
        ivArma = findViewById(R.id.ivArma);
        ivArma.setImageResource(MatchHelper.Cards.getImgByRef(murderCards.get(1)));
        ivHab = findViewById(R.id.ivHab);
        ivHab.setImageResource(MatchHelper.Cards.getImgByRef(murderCards.get(2)));

        //Reproducimos el audio aplausos
        mp = MediaPlayer.create(this, R.raw.aplausos);
        mp.start();
    }
    public void tapScreen(View view) {
        menu();
    }

    public void menu(){
        mp.release();
        mp = null;
        Intent menu = new Intent(this, ActivityMain.class);
        startActivity(menu);
    }
}