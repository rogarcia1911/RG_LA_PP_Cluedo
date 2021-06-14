package com.example.rg_la_pp_cluedo;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import com.example.rg_la_pp_cluedo.BBDD.Player;
import com.example.rg_la_pp_cluedo.BBDD.Room;
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class ActivityJuego extends AppCompatActivity {

    private ImageButton imBtPersonaje, imBtLugar, imBtArma, btnChat;
    private LinearLayout llhCartas;
    private LinearLayout llvCartas;
    private Button btnSuponer;
    private TextView tvCont;

    SharedPreferences shSettings, shPreferences, shGameMulti;
    DataBaseConnection firebaseConnection = null;
    DatabaseReference database, matchDataRef, roomRef;
    Match match;
    //Atributos MULTI
    Room room;
    String roomName, myTurn, status, userName;

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
        shGameMulti = getSharedPreferences(getString(R.string.PREFmultiGame),0);
        firebaseConnection = DataBaseConnection.getInstance();
        database = DataBaseConnection.getFirebase(getApplicationContext());

        userName = shSettings.getString("userName","");

        isNewMatch = getIntent().getBooleanExtra("gameNew",false);
        isSolo = getIntent().getBooleanExtra("gameMode",true);

        //Si pulsa el boton Back le llevará al ActivityMain
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(isSolo){
                    Intent inicio = new Intent(ActivityJuego.this, ActivityMain.class);
                    startActivity(inicio);
                } else {
                    LayoutInflater inflater = getLayoutInflater();
                    View view = inflater.inflate(R.layout.dialog_salir, null);

                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityJuego.this)
                            // .setView(view)
                            //TODO: traducir texto getString(R.string.msjSalirMulti)
                            .setMessage("Si sales ahora perderás la partida.\n¿Estas seguro de que quieres salir?")
                            .setPositiveButton(R.string.btPstv, new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    terminarPartidaMulti(false);
                                }
                            })
                            .setNegativeButton(R.string.btNgtv, null);

                    final AlertDialog dialog = builder.create();
                    dialog.show();

                }
            }
        };
        getOnBackPressedDispatcher().addCallback(callback);

        imBtPersonaje = findViewById(R.id.imBtPersonaje);
        imBtArma = findViewById(R.id.imBtArma);
        imBtLugar = findViewById(R.id.imBtHabitacion);
        tvCont = findViewById(R.id.txtV2);
        btnSuponer = findViewById(R.id.btnSuponer);
        btnChat = findViewById(R.id.btnChat);

        if(isNewMatch) {
            reiniciarCartas();
            //reiniciamos los marcadores de los 3 ActivtyElegir...
            reiniciarBtMarc(spEP);
            reiniciarBtMarc(spEH);
            reiniciarBtMarc(spEA);
            if (isSolo) {
                oportunidades = shPreferences.getInt("gameSoloCont", 0);
                cambiar_cont(shPreferences.getInt("gameSoloCont", 0)); //reiniciamos el contador
            }
        }
        if (isSolo){
            SharedPreferences spCont = getSharedPreferences("juegoDatos", Context.MODE_PRIVATE);
            contador = spCont.getInt("cont", oportunidades);
            cambiar_cont(contador);
        }

        SharedPreferences imagen1 = getSharedPreferences("img1", Context.MODE_PRIVATE);
        int imagen1Int = imagen1.getInt("img_per",  R.drawable.carta_interrogante);
        imagen_personaje = getIntent().getExtras().getInt("imagen_personaje", imagen1Int);
        elegir_per(imagen_personaje);

        SharedPreferences imagen2 = getSharedPreferences("img2", Context.MODE_PRIVATE);
        int imagen2Int = imagen2.getInt("img_ar", R.drawable.carta_interrogante);
        imagen_arma = getIntent().getIntExtra("imagen_arma", imagen2Int);
        elegir_ar(imagen_arma);

        SharedPreferences imagen3 = getSharedPreferences("img3", Context.MODE_PRIVATE);
        int imagen3Int = imagen3.getInt("img_lg", R.drawable.carta_interrogante);
        imagen_lugar = getIntent().getIntExtra("imagen_lugar", imagen3Int);
        elegir_lug(imagen_lugar);

        //Boton abre activity de elegir personaje
        imBtPersonaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity_personaje = new Intent(ActivityJuego.this, ActivityElegirPersonaje.class);
                activity_personaje.putExtra("gameMode",match.getIsSolo());
                startActivity(activity_personaje);
            }
        });

        //Boton abre activity de elegir arma
        imBtArma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity_arma = new Intent(ActivityJuego.this, ActivityElegirArma.class);
                activity_arma.putExtra("gameMode",match.getIsSolo());
                startActivity(activity_arma);
            }
        });

        //Boton abre activity de elegir habitacion
        imBtLugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity_habitacion = new Intent(ActivityJuego.this, ActivityElegirHabitacion.class);
                activity_habitacion.putExtra("gameMode",match.getIsSolo());
                startActivity(activity_habitacion);
            }
        });

        //Boton llama al método comprobar si ha elegido las 3 cartas
        btnSuponer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suponer();
            }
        });

        if (isSolo ){
            btnSuponer.setEnabled(true);
            getMatch();
            setupSolo();
        } else
            getRoom();


    }//FIN onCreate

    private void getRoom() {
        roomName = shGameMulti.getString("roomName", "");
        status = shGameMulti.getString("status", "");
        roomRef = database.getDatabase().getReference("Rooms/"+roomName);
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue()==null || !snapshot.exists())
                    return;

                myTurn = shGameMulti.getString("myTurn","");
                room = snapshot.getValue(Room.class);
                match = snapshot.child("match").getValue(Match.class);
                if (room!=null && room.getStatus()!=null){
                    if (status.contains(myTurn+":")) {
                        status = status.replace(myTurn+":","");
                        room.setStatus(status);
                        shGameMulti.edit().putString("status",status).apply();
                        roomRef.setValue(room);
                    } else if (!status.contains(myTurn+":") && room.getStatus().contains(myTurn+":")) {
                        status = room.getStatus().replace(myTurn+":","");
                        room.setStatus(status);
                        shGameMulti.edit().putString("status",status).apply();
                        roomRef.setValue(room);
                    }

                    if (match!= null && room.getWinner()!=null)
                        endMultiGame();
                    else
                        setupMulti();

                    btnSuponer.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*
        if( room == null )
            roomRef.get();
        else
            roomRef.setValue(room);*/
    }

    private void endMultiGame() {
        if (!room.getStatus().equals("Wait")){
            if (room.getWinner().equals(userName)) {
                Intent win = new Intent(ActivityJuego.this, ActivityGanar.class);
                win.putExtra("murderCards",murderedCards);
                win.putExtra("roomName",room.getName());
                startActivity(win);

            } else {
                Intent lose = new Intent(ActivityJuego.this, ActivityPerder.class);
                lose.putExtra("murderCards",murderedCards);
                lose.putExtra("roomName",room.getName());
                startActivity(lose);
            }
        } else {
            Intent inicio = new Intent(ActivityJuego.this, ActivityMain.class);
            startActivity(inicio);
        }

        shGameMulti.edit().clear().apply();
    }

    private void setupMulti() {

        if (match == null)  {

            room.setName(roomName);
            match = new Match();
            match.setName(MatchHelper.Mode.MULTI.name());
            match.setBeginningDate(System.currentTimeMillis()); //Con un new Date convertimos los milisegundos a fecha
            match.setIsSolo(false);
            match.setMurderCards(getIntent().getExtras().getIntegerArrayList("murderedCards"));
            room.setMatch(match);
            room.setStatus(status);
            roomRef.setValue(room);

        }

        murderedCards = match.getMurderCards();

        if (myTurn.equals(room.getStatus())){
            btnSuponer.setText(R.string.btSospechar);
            btnSuponer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    suponer();
                }
            });
        } else {
            btnSuponer.setText(R.string.btnWait);//TODO: Traducir texto
            btnSuponer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (room.getStatus().equals("Wait"))
                        Toast.makeText(getApplicationContext(), R.string.espera,Toast.LENGTH_SHORT).show();
                    else if (!room.getStatus().equals(myTurn))
                        Toast.makeText(getApplicationContext(), R.string.esperaTuTurno,Toast.LENGTH_SHORT).show();
                }
            });
        }

        btnChat.setVisibility(View.VISIBLE);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chat = new Intent(getApplicationContext(), ActivityChat.class);
                chat.putExtra("roomName", room.getName());
                chat.putExtra("gameMode", false);
                startActivity(chat);
            }
        });
        findViewById(R.id.txtV1).setVisibility(View.GONE);
        tvCont.setVisibility(View.GONE);
    }

    private void suponer() {

        if(imagen_personaje == R.drawable.carta_interrogante || imagen_arma == R.drawable.carta_interrogante || imagen_lugar == R.drawable.carta_interrogante)
            Toast.makeText(ActivityJuego.this, getString(R.string.msj3Cartas), Toast.LENGTH_SHORT).show();
        else if (murderedCards != null && !murderedCards.isEmpty())
            comprobar(imagen_personaje, imagen_arma, imagen_lugar);
        else if (isSolo)
            matchDataRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    murderedCards = task.getResult().getValue(Match.class).getMurderCards();
                    comprobar(imagen_personaje, imagen_arma, imagen_lugar);
                }
            });
        else {
            roomRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    Match match = task.getResult().child("match").getValue(Match.class);
                    murderedCards = match.getMurderCards();
                    comprobar(imagen_personaje, imagen_arma, imagen_lugar);
                }
            });
        }
    }

    private void setupSolo() {
        findViewById(R.id.txtV1).setVisibility(View.VISIBLE);
        tvCont.setVisibility(View.VISIBLE);
        btnChat.setVisibility(View.GONE);
    }

    private void getMatch() {
        btnSuponer.setEnabled(true);
        String userName = shSettings.getString("userName","");
        String matchName = shPreferences.getString("gameSoloName",""); // o recuperar la de Multi
        matchDataRef = database.getDatabase().getReference("Users/"+userName+"/Matchs/"+matchName);
        matchDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                match = snapshot.getValue(Match.class);
                if (match!=null){
                    if (murderedCards == null)
                        murderedCards = match.getMurderCards();
                    //Toast.makeText(getApplicationContext(), "isNewMatch  "+isNewMatch + " isSolo" + match.getIsSolo(), Toast.LENGTH_SHORT).show();

                    if (match.getEndingDate()!=0L){
                        if (match.getResultGame()) {
                            Intent win = new Intent(ActivityJuego.this, ActivityGanar.class);
                            win.putExtra("murderCards",murderedCards);
                            startActivity(win);
                        } else {
                            Intent lose = new Intent(ActivityJuego.this, ActivityPerder.class);
                            lose.putExtra("murderCards",murderedCards);
                            startActivity(lose);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(getApplicationContext(),"No se ha podido recuperar la partida.",Toast.LENGTH_SHORT).show();
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

        if (match!=null) {
            match.setCont(num);
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

        imBtLugar.setBackgroundResource((imagen_lugar));

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
        dialog.show();
    }

    /*Método comprueba si se ha elegido las cartas correctas
    y si no mostrará una de las incorectas llamando el métoco incorrecta */
    public void comprobar(int imagen_personaje, int imagen_arma, int imagen_lugar){
        reiniciarCartas(); //Reiniciar los imageButton
        //Bajar el contador de oportunidades
        if (isSolo){
            contador--;
            cambiar_cont(contador);
        } else {
            if (myTurn.equals("player1"))
                status = myTurn + ":player2";
            else
                status = myTurn + ":player1";
            room.setStatus(status);
            roomRef.setValue(room);
        }

        //comprobar si hay 1 correcta, comprobar si hay dos correctas o si estan todas correctas
        if(MatchHelper.Cards.getImgByRef(murderedCards.get(0)) == imagen_personaje &&
                MatchHelper.Cards.getImgByRef(murderedCards.get(1)) == imagen_arma &&
                MatchHelper.Cards.getImgByRef(murderedCards.get(2)) == imagen_lugar)
            if (isSolo)
                terminarPartida(true); //Terminamos la partida ganando y vamos a ActivityGanar
            else
                terminarPartidaMulti(true);
        else {
            //Si el contador es 0 termina la partida
            if(isSolo && contador <= 0)
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
        if (isSolo)
            terminarPartida(false);
        else
            terminarPartidaMulti(false);
        reiniciarCartas();
    }


    //Modifica fin, tiempoTot y Resultado de la última partida
    public void terminarPartida(boolean resultado) {
        SharedPreferences.Editor editor = shPreferences.edit();
        editor.remove("gameSoloName");
        editor.remove("gameSoloNum");
        editor.remove("gameSoloCont");
        editor.apply();
        match.setResultGame(resultado);
        match.setEndingDate(System.currentTimeMillis());

        matchDataRef.setValue(match);


    }//FIN terminarPartida

    private void terminarPartidaMulti(boolean resultado) {
        match.setEndingDate(System.currentTimeMillis());

        if (!room.getStatus().equals("Wait")){
            if (resultado)
                room.setWinner(userName);
            else {
                if (myTurn.equals("player1"))
                    room.setWinner(room.getPlayer2());
                else
                    room.setWinner(room.getPlayer1());
            }
            DatabaseReference userTask = database.getDatabase().getReference("Users/" + userName + "/User");
            userTask.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (room == null) return;
                    User user = task.getResult().getValue(User.class);
                    Integer num = user.getNumMultiMatchs() + 1;
                    String otherUserName;

                    Match meMatch = match;
                    Match otherMatch = match;

                    if (room.getWinner().equals(userName)) {
                        meMatch.setResultGame(true);
                        otherMatch.setResultGame(false);
                    } else {
                        meMatch.setResultGame(false);
                        otherMatch.setResultGame(true);
                    }

                    if ( room.getPlayer2()==null &&  room.getPlayer1()==null )
                        room=null; //borramos la sala

                    if (myTurn.equals("player1")){
                        room.setPlayer1( null); //borramos un jugador
                        otherUserName = room.getPlayer2();
                    }else{
                        room.setPlayer2(null); //borramos un jugador
                        otherUserName = room.getPlayer1();
                    }

                    meMatch.setName(match.getName()+"-"+num);
                    meMatch.setNum(num);
                    database.getDatabase().getReference("Users/" + userName + "/Matchs/MULTI-" + num).setValue(meMatch);
                    user.setNumMultiMatchs(num);
                    userTask.setValue(user);//Actualizamos el número de partidas multi

                    DatabaseReference otherUserTask = database.getDatabase().getReference("Users/" + otherUserName + "/User");
                    otherUserTask.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.getResult().getValue(User.class)==null) return;
                            User otherUser = task.getResult().getValue(User.class);
                            Integer otherNum = otherUser.getNumMultiMatchs() + 1;
                            otherMatch.setNum(otherNum);
                            otherMatch.setName(match.getName()+"-"+num);
                            database.getDatabase().getReference("Users/"+otherUserName+"/Matchs/MULTI-"+num).setValue(otherMatch);

                            otherUser.setNumMultiMatchs(otherNum);
                            otherUserTask.setValue(otherUser);
                        }
                    });
                }
            });

            room.setMatch(match);
        } else {
            Intent inicio = new Intent(ActivityJuego.this, ActivityMain.class);
            startActivity(inicio);
            room=null; //borramos la sala

            shGameMulti.edit().clear().apply();
        }

        roomRef.setValue(room);

    }// FIN terminarPartidaMulti

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