package com.example.rg_la_pp_cluedo;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class ActivityPerder extends AppCompatActivity {

    private ImageView ivPers,ivArma,ivHab;

    private SoundPool sp;
    int sonido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perder);

        ArrayList<Integer> murderCards = getIntent().getExtras().getIntegerArrayList("murderCards");
        //TODO: mostrar las culpables
        ivPers = findViewById(R.id.ivPers3);
        ivPers.setImageResource(MatchHelper.Cards.getImgByRef(murderCards.get(0)));
        ivArma = findViewById(R.id.ivArma3);
        ivArma.setImageResource(MatchHelper.Cards.getImgByRef(murderCards.get(1)));
        ivHab = findViewById(R.id.ivHab3);
        ivHab.setImageResource(MatchHelper.Cards.getImgByRef(murderCards.get(2)));

        //Reproducimos el audio aplausos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            sp = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            sp= new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
        }
        sonido = sp.load(this, R.raw.trompeta_militar, 1);
        sonido();

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

    private void sonido() {
        sp.play(sonido, 1, 1, 1, 0, 0);
    }

    public void tapScreen(View view) {
        //sp.release();
        //sp = null;
        //sonido();
        Intent inicio = new Intent(this, ActivityMain.class);
        startActivity(inicio);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sp.release();
        sp = null;
    }
}