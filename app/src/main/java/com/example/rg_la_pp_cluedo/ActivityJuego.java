package com.example.rg_la_pp_cluedo;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ActivityJuego extends AppCompatActivity {

    private ImageButton imBtPersonaje, imBtLugar, imBtArma;
    private ImageView mostrarimagen;
    private Button btnSuponer;
    private TextView tvCont;

    private String fich = "cartas.dat";
    private int oportunidades = 10, contador;
    private int imagen_personaje, imagen_arma, imagen_lugar;
    //nombre de SharedPreferences de los ActivityElegir...
    private String spEP = "datosEP",spEH = "datosEH",spEA = "datosEA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

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

        if(getIntent().getBooleanExtra("nuevaPartida",false)) {
            reiniciarCartas();
            //reiniciamos los marcadores de los 3 ActivtyElegir...
            reiniciarBtMarc(spEP);
            reiniciarBtMarc(spEH);
            reiniciarBtMarc(spEA);
            cambiar_cont(oportunidades); //reiniciamos el contador
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
                else
                    comprobar(imagen_personaje, imagen_arma, imagen_lugar);
            }
        });

    }//FIN onCreate

    //Método que modifica el tvCont
    public void cambiar_cont(int num){

        tvCont.setText(String.valueOf(num));

        SharedPreferences spCont = getSharedPreferences("juegoDatos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editCont = spCont.edit();
        editCont.putInt( "cont", num);
        editCont.commit();
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
    public void incorrecta(int img){
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityJuego.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_incorrecto, null);

        mostrarimagen = view.findViewById(R.id.iv_incorrecta);
        mostrarimagen.setImageResource(img);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();

    }

    /*Método comprueba si se ha elegido las cartas correctas
    y si no mostrará una de las incorectas llamando el métoco incorrecta */
    public void comprobar(int imagen_personaje, int imagen_arma, int imagen_lugar){
        reiniciarCartas(); //Reiniciar los imageButton
        //Bajar el contador de oportunidades
        contador--;
        cambiar_cont(contador);

        Carta ca1 = null, ca2 = null, ca3 = null;

        try {
            ObjectInputStream ois = new ObjectInputStream(openFileInput(fich));
            Carta[] cartas = (Carta[]) ois.readObject();

            for (int i=0 ; i<cartas.length ; i++) {
                if(i<6 && cartas[i].isCulpable()) ca1 = cartas[i];
                if(i>=6 && i<=11 && cartas[i].isCulpable()) ca2 = cartas[i];
                if(i>11 && cartas[i].isCulpable()) ca3 = cartas[i];
            }

            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //Array con las 3 cartas culpables
        Carta[] c = {ca1, ca2, ca3};

        //comprobar si hay 1 correcta, comprobar si hay dos correctas o si estan todas correctas
        if(c[0].getImagen() == imagen_personaje && c[1].getImagen() == imagen_arma && c[2].getImagen() == imagen_lugar){
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
                ArrayList<Integer> cartasInc = new ArrayList<Integer>();
                if(c[0].getImagen() != imagen_personaje) cartasInc.add(imagen_personaje);
                if (c[1].getImagen() != imagen_arma)cartasInc.add(imagen_arma);
                if (c[2].getImagen() != imagen_lugar)cartasInc.add(imagen_lugar);

                //Llamamos el método incorecta para mostrar una de las cartas incorrectas
                int aleatorio = (int) (Math.random()*cartasInc.size());
                incorrecta(cartasInc.get(aleatorio));
            }

        }

    }//FIN comprobar

    //Modifica fin, tiempoTot y Resultado de la última partida
    public void terminarPartida(boolean resultado) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(
                this, "administracion", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("SELECT MAX(id), inicio, datetime()," +
                "strftime('%s',datetime())-strftime('%s',inicio) " +
                "FROM partidas",null);
        fila.moveToFirst();

        int idPartida = fila.getInt(0);
        String inicio = fila.getString(1);
        String time = fila.getString(2);
        String diferencia = calcTiempodeSeg(fila.getInt(3));

        ContentValues registro = new ContentValues();
        registro.put("id",idPartida);
        registro.put("inicio", inicio);
        registro.put("fin", time);
        registro.put("tiempoTot", diferencia);
        registro.put("resultado", resultado);

        db.update("partidas", registro,"id="+idPartida,null);

        db.close();
        admin.close();
    }//FIN terminarPartida

    private String calcTiempodeSeg(int seg) {
        String segS,minS = null,horS = null;
        int min = 0, hor = 0, di;
        if (seg>60) { //Calculamos minutos y segundos restantes
            seg = seg%60;
            min = seg/60;

            if(min>60) { //Calculamos horas y minutos restantes
                min = min%60;
                hor = min/60;

                //Si ha pasado 24h devuelve el string dentro del if
                //Si no devuelve el String al final del metodo fuera de todos los if
                if(hor>24) { //Calculamos dias y horas restantes
                    hor = hor%24;
                    di = hor/24;

                    if(seg<10) segS = "0"+seg;
                    else segS= String.valueOf(seg);

                    if(min<10) minS = "0"+min;
                    else minS= String.valueOf(min);

                    if(hor<10) horS = "0"+hor;
                    else horS= String.valueOf(hor);

                    return di+"D  "+horS+":"+minS+":"+segS;
                }

            }
        }

        if(seg<10) segS= "0"+seg;
        else segS= String.valueOf(seg);
        if(min<10) minS= "0"+min;
        else minS= String.valueOf(min);
        if(hor<10) horS= "0"+hor;
        else horS= String.valueOf(hor);

        //devolvemos tiempo sin dias
        if (segS!=null)
            return horS+":"+minS+":"+segS;
        else
            return null;

    }

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

    //Método del botón Rendirse
    public void rendirse(View view) {
        Intent perder = new Intent(this, ActivityPerder.class);
        startActivity(perder);

        terminarPartida(false);
        reiniciarCartas();
    }

}