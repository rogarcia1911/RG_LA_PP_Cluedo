package com.example.rg_la_pp_cluedo;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.rg_la_pp_cluedo.BBDD.DataBaseConnection;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ActivityPerder extends AppCompatActivity {

    DataBaseConnection firebaseConnection = null;
    DatabaseReference database;
    private MediaPlayer mp;
    private ImageView ivPers,ivArma,ivHab;

    private SoundPool sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perder);
        firebaseConnection = DataBaseConnection.getInstance();
        database = DataBaseConnection.getFirebase(getApplicationContext());
        mp = MediaPlayer.create(this, R.raw.trompeta_militar);
        mp.start();
/*
        String roomName = (getIntent().hasExtra("roomName")) ? getIntent().getStringExtra("roomName") : null;
        if (roomName!=null)
            database.getDatabase().getReference("Rooms/"+roomName).removeValue();
*/
        ArrayList<Integer> murderCards = getIntent().getExtras().getIntegerArrayList("murderCards");
        //TODO: mostrar las culpables
        ivPers = findViewById(R.id.ivPers3);
        ivPers.setImageResource(MatchHelper.Cards.getImgByRef(murderCards.get(0)));
        ivArma = findViewById(R.id.ivArma3);
        ivArma.setImageResource(MatchHelper.Cards.getImgByRef(murderCards.get(1)));
        ivHab = findViewById(R.id.ivHab3);
        ivHab.setImageResource(MatchHelper.Cards.getImgByRef(murderCards.get(2)));

        //Si pulsa el boton Back le llevará al ActivityMain
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent inicio = new Intent(ActivityPerder.this, ActivityMain.class);
                startActivity(inicio);
            }
        };
        getOnBackPressedDispatcher().addCallback(callback);
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