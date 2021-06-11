package com.example.rg_la_pp_cluedo;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rg_la_pp_cluedo.BBDD.Card;
import com.example.rg_la_pp_cluedo.BBDD.DataBaseConnection;
import com.example.rg_la_pp_cluedo.BBDD.Match;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class ActivityJuego extends AppCompatActivity {

    private ImageButton imBtPersonaje, imBtLugar, imBtArma;
    private LinearLayout llhCartas;
    private LinearLayout llvCartas;
    private Button btnSuponer, btnChat, btnGame;
    private TextView tvCont;

    SharedPreferences shSettings, shPreferences, shGameSolo, shGameMulti;
    DataBaseConnection firebaseConnection = null;
    DatabaseReference database, matchDataRef;
    Match match;
    String matchName;

    private Boolean isSolo, isNewMatch;
    private int oportunidades, contador;
    private int imagen_personaje, imagen_arma, imagen_lugar;
    //nombre de SharedPreferences de los ActivityElegir...
    private String spEP = "datosEP",spEH = "datosEH",spEA = "datosEA";
    private ArrayList<Integer> murderedCards;

    //TODO: en el primer sospechar no cambia el contador
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        shSettings = getSharedPreferences(getString(R.string.PREFsetttings), 0);
        shPreferences = getSharedPreferences(getString(R.string.PREFapp),0);
        shGameSolo = getSharedPreferences(getString(R.string.PREFsoloGame), Context.MODE_PRIVATE);
        shGameMulti = getSharedPreferences(getString(R.string.PREFmultiGame), Context.MODE_PRIVATE);
        firebaseConnection = DataBaseConnection.getInstance();
        database = DataBaseConnection.getFirebase(getApplicationContext());

        //TODO: preferancias idioma y sonido
        shSettings.getString("appLanguage","");
        shSettings.getBoolean("appSound",true);

        isNewMatch = getIntent().getBooleanExtra("gameNew",false);
        isSolo = getIntent().getBooleanExtra("gameMode",true);

        //Si pulsa el boton Back le llevará al ActivityMain
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent inicio = new Intent(ActivityJuego.this, ActivityMain.class);
                startActivity(inicio);
            }
        };
        getOnBackPressedDispatcher().addCallback(callback);

        imBtPersonaje = findViewById(R.id.imBtPersonaje);
        imBtArma = findViewById(R.id.imBtArma);
        imBtLugar = findViewById(R.id.imBtHabitacion);
        tvCont = findViewById(R.id.txtV2);
        btnSuponer = findViewById(R.id.btnSuponer);
        btnChat = findViewById(R.id.btnChat);
        btnGame = findViewById(R.id.btnGame);

        if(isNewMatch) {
            reiniciarCartas();
            //reiniciamos los marcadores de los 3 ActivtyElegir...
            reiniciarBtMarc(spEP);
            reiniciarBtMarc(spEH);
            reiniciarBtMarc(spEA);
            oportunidades = shPreferences.getInt("gameSoloCont",0);
            cambiar_cont(shPreferences.getInt("gameSoloCont",0)); //reiniciamos el contador
        }

        SharedPreferences spCont = getSharedPreferences("juegoDatos", Context.MODE_PRIVATE);
        contador = spCont.getInt("cont", oportunidades);
        cambiar_cont(contador);

        SharedPreferences imagen1 = getSharedPreferences("img1", Context.MODE_PRIVATE);
        int imagen1Int = imagen1.getInt("img_per",  R.drawable.carta_interrogante);
        imagen_personaje = getIntent().getIntExtra("imagen_personaje", imagen1Int);
        elegir_per(imagen_personaje);

        SharedPreferences imagen2 = getSharedPreferences("img2", Context.MODE_PRIVATE);
        int imagen2Int = imagen2.getInt("img_ar", R.drawable.carta_interrogante);
        imagen_arma = getIntent().getIntExtra("imagen_arma", imagen2Int);
        elegir_ar(imagen_arma);

        SharedPreferences imagen3 = getSharedPreferences("img3", Context.MODE_PRIVATE);
        int imagen3Int = imagen3.getInt("img_lg", R.drawable.carta_interrogante);
        imagen_lugar = getIntent().getIntExtra("imagen_lugar", imagen3Int);
        elegir_lug(imagen_lugar);

        getMatch();

        //Boton abre activity de elegir personaje
        imBtPersonaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity_personaje = new Intent(ActivityJuego.this, ActivityElegirPersonaje.class);
                startActivity(activity_personaje);
            }
        });

        //Boton abre activity de elegir arma
        imBtArma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity_arma = new Intent(ActivityJuego.this, ActivityElegirArma.class);
                startActivity(activity_arma);
            }
        });

        //Boton abre activity de elegir habitacion
        imBtLugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity_habitacion = new Intent(ActivityJuego.this, ActivityElegirHabitacion.class);
                startActivity(activity_habitacion);
            }
        });

        //Boton llama al método comprobar si ha elegido las 3 cartas
        btnSuponer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imagen_personaje == R.drawable.carta_interrogante || imagen_arma == R.drawable.carta_interrogante || imagen_lugar == R.drawable.carta_interrogante)
                    Toast.makeText(ActivityJuego.this, getString(R.string.msj3Cartas), Toast.LENGTH_SHORT).show();
                else{
                    matchDataRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            match = task.getResult().getValue(Match.class);
                            murderedCards = match.getMurderCards();

                            if(isSolo){
                                comprobarIndividual(imagen_personaje, imagen_arma, imagen_lugar);
                            }else{
                                comprobarMultijugador(imagen_personaje, imagen_arma, imagen_lugar);
                            }
                        }
                    });

                }


            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chat = new Intent(getApplicationContext(), ActivityChat.class);
                startActivity(chat);
            }
        });

    }//FIN onCreate

    private void getMatch() {
        String userName = shSettings.getString("userName","");
        String matchName = shPreferences.getString("gameSoloName",""); // o recuperar la de Multi
        matchDataRef = database.getDatabase().getReference("Users/"+userName+"/Matchs/"+matchName);
        matchDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                match = snapshot.getValue(Match.class);
                if (match!=null){
                    if (murderedCards== null)
                        murderedCards = match.getMurderCards();
                    Toast.makeText(getApplicationContext(), "isNewMatch  "+isNewMatch + " isSolo" + match.getIsSolo(), Toast.LENGTH_SHORT).show();

                    if (match.getEndingDate()!=0L){
                        if (match.getResultGame()) {
                            Intent win = new Intent(ActivityJuego.this, ActivityGanar.class);
                            startActivity(win);
                        } else {
                            Intent lose = new Intent(ActivityJuego.this, ActivityPerder.class);
                            startActivity(lose);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"No se ha podido recuperar la partida.",Toast.LENGTH_SHORT).show();
            }
        });
        if( match == null )
            matchDataRef.get();
        else
            matchDataRef.setValue(match);
    }

    //Método que modifica el tvCont
    public void cambiar_cont(int num){
        tvCont.setText(String.valueOf(num));

        SharedPreferences spCont = getSharedPreferences("juegoDatos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editCont = spCont.edit();
        editCont.putInt( "cont", num);
        editCont.commit();

        if (match!=null && matchDataRef != null) {
            match.setCont(num);
            matchDataRef.setValue(match);
        }
    }

    //Método que modifica el imBtPersonaje
    public void elegir_per(int imagen_personaje){

        imBtPersonaje.setBackgroundResource(imagen_personaje);

        SharedPreferences pref1 = getSharedPreferences("img1", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit1 = pref1.edit();
        edit1.putInt( "img_per", imagen_personaje);
        edit1.commit();
    }

    //Método que modifica el imBtArma
    public void elegir_ar(int imagen_arma){

        imBtArma.setBackgroundResource(imagen_arma);

        SharedPreferences pref2 = getSharedPreferences("img2", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit2 = pref2.edit();
        edit2.putInt( "img_ar", imagen_arma);
        edit2.commit();
    }

    //Método que modifica el imBtHabitacion
    public void elegir_lug(int imagen_lugar){

        imBtLugar.setBackgroundResource(imagen_lugar);

        SharedPreferences pref3 = getSharedPreferences("img3", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit3 = pref3.edit();
        edit3.putInt( "img_lg", imagen_lugar);
        edit3.commit();
    }

    //Método crea AlertDialog que muestra una de las imagenes incorrectas elegidas
    public void incorrecta(ArrayList<Integer> images){
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityJuego.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_incorrecto, null);
        // LinearLayout donde se irán agregando las cartas incorrectas
        llhCartas = view.findViewById(R.id.llhcartas);
        llvCartas = view.findViewById(R.id.llvcartas);
        for (int i = 0 ; i < images.size() ; i++ )
        {
            ImageView card = new ImageView(ActivityJuego.this);
            card.setImageResource(images.get(i));
            card.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            if (i<2)
                llhCartas.addView(card);
            else
                llvCartas.addView(card);
        }
        builder.setView(view);
        builder.setNegativeButton(R.string.btNext, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog dialog = builder.create();
        /*dialog.setButton(R.id.btNext, getString(R.string.btNext), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });*/
        dialog.show();
    }

    /*Método comprueba si se ha elegido las cartas correctas
    y si no mostrará una de las incorectas llamando el métoco incorrecta */
    public void comprobarIndividual(int imagen_personaje, int imagen_arma, int imagen_lugar){
        reiniciarCartas(); //Reiniciar los imageButton
        //Bajar el contador de oportunidades
        contador--;
        cambiar_cont(contador);

        //comprobar si hay 1 correcta, comprobar si hay dos correctas o si estan todas correctas
        if(MatchHelper.Cards.getImgByRef(murderedCards.get(0)) == imagen_personaje &&
                MatchHelper.Cards.getImgByRef(murderedCards.get(1)) == imagen_arma &&
                MatchHelper.Cards.getImgByRef(murderedCards.get(2)) == imagen_lugar)
            terminarPartida(true); //Terminamos la partida ganando y vamos a ActivityGanar
        else {
            //Si el contador es 0 termina la partida
            if(contador <= 0)
                terminarPartida(false); //Modificamos la base de datos
            else {
                //Metemos las imagenes de las cartas incorrectas en un array
                ArrayList<Integer> cartasInc = new ArrayList<Integer>();
                if (MatchHelper.Cards.getImgByRef(murderedCards.get(0)) != imagen_personaje) cartasInc.add(imagen_personaje);
                if (MatchHelper.Cards.getImgByRef(murderedCards.get(1)) != imagen_arma) cartasInc.add(imagen_arma);
                if (MatchHelper.Cards.getImgByRef(murderedCards.get(2)) != imagen_lugar) cartasInc.add(imagen_lugar);

                //Llamamos el método incorecta para mostrar las cartas incorrectas
                incorrecta(cartasInc); //TODO: para que no se cierre tan rapido algunas veces hay que ponerlo en el task de firebase
            }

        }

    }//FIN comprobar

    //Método del botón Rendirse
    public void rendirse(View view) {
        terminarPartida(false);
        reiniciarCartas();
    }

    private void comprobarMultijugador(int person, int weapon, int place) {
        reiniciarCartas();
        contador--;
        cambiar_cont(contador);
        Card card1 = null, card2 = null, card3 = null;

        cardList(); //Contains murdered cards


        if(MatchHelper.Cards.getImgByRef((Integer) murderedCards.get(0)) == person &&
                MatchHelper.Cards.getImgByRef((Integer) murderedCards.get(1)) == weapon &&
                MatchHelper.Cards.getImgByRef((Integer) murderedCards.get(2)) == place){
            terminarPartida(true); //Modificamos la base de datos

            Intent win = new Intent(ActivityJuego.this, ActivityGanar.class);
            startActivity(win);

        } else {
            //Si el contador es 0 termina la partida
            if(contador == 0){
                terminarPartida(false); //Modificamos la base de datos

                Intent lose = new Intent(ActivityJuego.this, ActivityPerder.class);
                startActivity(lose);
            } else{

                //Metemos las imagenes de las cartas incorrectas en un array
                ArrayList<Integer> incorrectCards = new ArrayList<Integer>();
                if(MatchHelper.Cards.getImgByRef((Integer) murderedCards.get(0)) != imagen_personaje) incorrectCards.add(person);
                if (MatchHelper.Cards.getImgByRef((Integer) murderedCards.get(1)) != imagen_arma) incorrectCards.add(weapon);
                if (MatchHelper.Cards.getImgByRef((Integer) murderedCards.get(2)) != imagen_lugar) incorrectCards.add(place);

                //Llamamos el método incorecta para mostrar las cartas incorrectas
                incorrecta(incorrectCards);
            }

        }


    }

    /**
     * Check if this node is != null and transform array in arrayList
     */
    private void cardList() {
        matchDataRef
            .child("Match").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    murderedCards = null;
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                        Match currentMatch = objSnapshot.getValue(Match.class);

                        if (currentMatch != null){
                            murderedCards = currentMatch.getMurderCards();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }


    //Modifica fin, tiempoTot y Resultado de la última partida
    public void terminarPartida(boolean resultado) {
        SharedPreferences.Editor editor = shPreferences.edit();
        if (isSolo) {
            editor.remove("gameSoloName");
            editor.remove("gameSoloNum");
            editor.remove("gameSoloCont");
            shGameSolo.edit().clear().apply();
        } else {
            editor.remove("gameMultiName");
            editor.remove("gameMultiNum");
            shGameMulti.edit().clear().apply();
        }
        editor.apply();
        match.setResultGame(resultado);
        match.setEndingDate(System.currentTimeMillis());

        matchDataRef.setValue(match);


    }//FIN terminarPartida

    //Método reinicia todas las imagenes
    public void reiniciarCartas() {
        imagen_personaje = R.drawable.carta_interrogante;
        imagen_arma = R.drawable.carta_interrogante;
        imagen_lugar = R.drawable.carta_interrogante;

        elegir_per(R.drawable.carta_interrogante);
        elegir_ar(R.drawable.carta_interrogante);
        elegir_lug(R.drawable.carta_interrogante);
    }

    public void reiniciarBtMarc(String nameSP) {
        SharedPreferences sp = getSharedPreferences(nameSP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.clear();
        editor.commit();
    }

}