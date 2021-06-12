package com.example.rg_la_pp_cluedo;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rg_la_pp_cluedo.BBDD.Match;

import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class ActivityGanar extends AppCompatActivity {

    private ImageView ivPers,ivArma,ivHab;

    private String fich = "cartas.dat";
    private SoundPool sp;
    private int sonido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganar);

        //Si pulsa el boton Back le llevará al ActivityMain
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent inicio = new Intent(ActivityGanar.this, ActivityMain.class);
                startActivity(inicio);
            }
        };
        getOnBackPressedDispatcher().addCallback(callback);

        ArrayList<Integer> murderCards = getIntent().getExtras().getIntegerArrayList("murderCards");
        //TODO: mostrar las culpables

        ivPers = findViewById(R.id.ivPers);
        ivPers.setImageResource(MatchHelper.Cards.getImgByRef(murderCards.get(0)));
        ivArma = findViewById(R.id.ivArma);
        ivArma.setImageResource(MatchHelper.Cards.getImgByRef(murderCards.get(1)));
        ivHab = findViewById(R.id.ivHab);
        ivHab.setImageResource(MatchHelper.Cards.getImgByRef(murderCards.get(2)));

        //Reproducimos el audio aplausos
        sp= new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
        sonido = sp.load(this, R.raw.aplausos, 1);
        sp.play(sonido, 1, 1, 1, 0, 0);
    }

    public void tapScreen(View view) {
        sp.stop(sonido);
        //sp.unload(sonido);
        Intent inicio = new Intent(this, ActivityMain.class);
        startActivity(inicio);
    }
}