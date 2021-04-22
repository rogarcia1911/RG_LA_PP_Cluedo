package com.example.rg_la_pp_cluedo;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ActivityElegirHabitacion extends AppCompatActivity {

    private ImageButton imgbtn_lug1, imgbtn_lug2, imgbtn_lug3, imgbtn_lug4, imgbtn_lug5, imgbtn_lug6;
    private ImageButton imbtn_verdadero1, imbtn_falso1, imbtn_inter1,
            imbtn_verdadero2, imbtn_falso2, imbtn_inter2,
            imbtn_verdadero3, imbtn_falso3, imbtn_inter3,
            imbtn_verdadero4, imbtn_falso4, imbtn_inter4,
            imbtn_verdadero5, imbtn_falso5, imbtn_inter5,
            imbtn_verdadero6, imbtn_falso6, imbtn_inter6;

    private String nameSP = "datosEH"; //nombre del SharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir_habitacion);

        imgbtn_lug1 = findViewById(R.id.imgbtn_lugar1);
        imgbtn_lug1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_ppl(R.drawable.lugar_banio);
            }
        });

        imgbtn_lug2 = findViewById(R.id.imgbtn_lugar2);
        imgbtn_lug2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_ppl(R.drawable.lugar_comedor);
            }
        });

        imgbtn_lug3 = findViewById(R.id.imgbtn_lugar3);
        imgbtn_lug3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_ppl(R.drawable.lugar_dormitorio);
            }
        });

        imgbtn_lug4 = findViewById(R.id.imgbtn_lugar4);
        imgbtn_lug4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_ppl(R.drawable.lugar_estudio);
            }
        });

        imgbtn_lug5 = findViewById(R.id.imgbtn_lugar5);
        imgbtn_lug5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_ppl(R.drawable.lugar_garaje);
            }
        });

        imgbtn_lug6 = findViewById(R.id.imgbtn_lugar6);
        imgbtn_lug6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_ppl(R.drawable.lugar_patio);
            }
        });

        imbtn_verdadero1 = findViewById(R.id.img_tick1);
        imbtn_verdadero1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //llamamos el método marcar que cambiara el estado del imageButton y se guardará en un SharedPreferences
                if (imbtn_verdadero1.getAlpha()==1)
                    desmarcar(imbtn_verdadero1,"opaV1");
                else
                    marcar(imbtn_verdadero1,imbtn_falso1,imbtn_inter1,
                            "opaV1","opaF1","opaI1");
            }
        });
        imbtn_falso1 = findViewById(R.id.img_x1);
        imbtn_falso1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imbtn_falso1.getAlpha()==1)
                    desmarcar(imbtn_falso1,"opaF1");
                else
                    marcar(imbtn_falso1,imbtn_verdadero1,imbtn_inter1,
                            "opaF1","opaV1","opaI1");
            }
        });
        imbtn_inter1 = findViewById(R.id.img_interrogación1);
        imbtn_inter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imbtn_inter1.getAlpha()==1)
                    desmarcar(imbtn_inter1,"opaI1");
                else
                    marcar(imbtn_inter1,imbtn_verdadero1,imbtn_falso1,
                            "opaI1","opaV1","opaF1");
            }
        });


        imbtn_verdadero2 = findViewById(R.id.img_tick2);
        imbtn_verdadero2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //llamamos el método marcar que cambiara el estado del imageButton y se guardará en un SharedPreferences
                if (imbtn_verdadero2.getAlpha()==1)
                    desmarcar(imbtn_verdadero2,"opaV2");
                else
                    marcar(imbtn_verdadero2,imbtn_falso2,imbtn_inter2,
                            "opaV2","opaF2","opaI2");
            }
        });
        imbtn_falso2 = findViewById(R.id.img_x2);
        imbtn_falso2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imbtn_falso2.getAlpha()==1)
                    desmarcar(imbtn_falso2,"opaF2");
                else
                    marcar(imbtn_falso2,imbtn_verdadero2,imbtn_inter2,
                            "opaF2","opaV2","opaI2");
            }
        });
        imbtn_inter2 = findViewById(R.id.img_interrogación2);
        imbtn_inter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imbtn_inter2.getAlpha()==1)
                    desmarcar(imbtn_inter2,"opaI2");
                else
                    marcar(imbtn_inter2,imbtn_verdadero2,imbtn_falso2,
                            "opaI2","opaV2","opaF2");
            }
        });


        imbtn_verdadero3 = findViewById(R.id.img_tick3);
        imbtn_verdadero3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //llamamos el método marcar que cambiara el estado del imageButton y se guardará en un SharedPreferences
                if (imbtn_verdadero3.getAlpha()==1)
                    desmarcar(imbtn_verdadero3,"opaV3");
                else
                    marcar(imbtn_verdadero3,imbtn_falso3,imbtn_inter3,
                            "opaV3","opaF3","opaI3");
            }
        });
        imbtn_falso3 = findViewById(R.id.img_x3);
        imbtn_falso3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imbtn_falso3.getAlpha()==1)
                    desmarcar(imbtn_falso3,"opaF3");
                else
                    marcar(imbtn_falso3,imbtn_verdadero3,imbtn_inter3,
                            "opaF3","opaV3","opaI3");
            }
        });
        imbtn_inter3 = findViewById(R.id.img_interrogación3);
        imbtn_inter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imbtn_inter3.getAlpha()==1)
                    desmarcar(imbtn_inter3,"opaI3");
                else
                    marcar(imbtn_inter3,imbtn_verdadero3,imbtn_falso3,
                            "opaI3","opaV3","opaF3");
            }
        });

        imbtn_verdadero4 = findViewById(R.id.img_tick4);
        imbtn_verdadero4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //llamamos el método marcar que cambiara el estado del imageButton y se guardará en un SharedPreferences
                if (imbtn_verdadero4.getAlpha()==1)
                    desmarcar(imbtn_verdadero4,"opaV4");
                else
                    marcar(imbtn_verdadero4,imbtn_falso4,imbtn_inter4,
                            "opaV4","opaF4","opaI4");
            }
        });
        imbtn_falso4 = findViewById(R.id.img_x4);
        imbtn_falso4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imbtn_falso4.getAlpha()==1)
                    desmarcar(imbtn_falso4,"opaF4");
                else
                    marcar(imbtn_falso4,imbtn_verdadero4,imbtn_inter4,
                            "opaF4","opaV4","opaI4");
            }
        });
        imbtn_inter4 = findViewById(R.id.img_interrogación4);
        imbtn_inter4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imbtn_inter4.getAlpha()==1)
                    desmarcar(imbtn_inter4,"opaI4");
                else
                    marcar(imbtn_inter4,imbtn_verdadero4,imbtn_falso4,
                            "opaI4","opaV4","opaF4");
            }
        });

        imbtn_verdadero5 = findViewById(R.id.img_tick5);
        imbtn_verdadero5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //llamamos el método marcar que cambiara el estado del imageButton y se guardará en un SharedPreferences
                if (imbtn_verdadero5.getAlpha()==1)
                    desmarcar(imbtn_verdadero5,"opaV5");
                else
                    marcar(imbtn_verdadero5,imbtn_falso5,imbtn_inter5,
                            "opaV5","opaF5","opaI5");
            }
        });
        imbtn_falso5 = findViewById(R.id.img_x5);
        imbtn_falso5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imbtn_falso5.getAlpha()==1)
                    desmarcar(imbtn_falso5,"opaF5");
                else
                    marcar(imbtn_falso5,imbtn_verdadero5,imbtn_inter5,
                            "opaF5","opaV5","opaI5");
            }
        });
        imbtn_inter5 = findViewById(R.id.img_interrogación5);
        imbtn_inter5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imbtn_inter5.getAlpha()==1)
                    desmarcar(imbtn_inter5,"opaI5");
                else
                    marcar(imbtn_inter5,imbtn_verdadero5,imbtn_falso5,
                            "opaI5","opaV5","opaF5");
            }
        });

        imbtn_verdadero6 = findViewById(R.id.img_tick6);
        imbtn_verdadero6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //llamamos el método marcar que cambiara el estado del imageButton y se guardará en un SharedPreferences
                if (imbtn_verdadero6.getAlpha()==1)
                    desmarcar(imbtn_verdadero6,"opaV6");
                else
                    marcar(imbtn_verdadero6,imbtn_falso6,imbtn_inter6,
                            "opaV6","opaF6","opaI6");
            }
        });
        imbtn_falso6 = findViewById(R.id.img_x6);
        imbtn_falso6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imbtn_falso6.getAlpha()==1)
                    desmarcar(imbtn_falso6,"opaF6");
                else
                    marcar(imbtn_falso6,imbtn_verdadero6,imbtn_inter6,
                            "opaF6","opaV6","opaI6");
            }
        });
        imbtn_inter6 = findViewById(R.id.img_interrogación6);
        imbtn_inter6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imbtn_inter6.getAlpha()==1)
                    desmarcar(imbtn_inter6,"opaI6");
                else
                    marcar(imbtn_inter6,imbtn_verdadero6,imbtn_falso6,
                            "opaI6","opaV6","opaF6");
            }
        });

        marcarBotones(nameSP);

    }

    public void activity_ppl(int aux){
        Intent main = new Intent(ActivityElegirHabitacion.this, ActivityJuego
                .class);
        main.putExtra("imagen_lugar", aux);

        startActivity(main);
    }

    public void marcar(ImageButton ibMarcar,ImageButton ibDesmarcar1,ImageButton ibDesmarcar2,
                       String sMarcar,String sDesmarcar1,String sDesmarcar2) {
        SharedPreferences shp = getSharedPreferences(nameSP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();

        editor.putFloat(sMarcar, 1); //key , value
        editor.putFloat(sDesmarcar1, (float) 0.4); //key , value
        editor.putFloat(sDesmarcar2, (float) 0.4); //key , value
        editor.commit();

        ibMarcar.setAlpha(shp.getFloat(sMarcar, (float) 0.4)); //key , defValue
        ibDesmarcar1.setAlpha(shp.getFloat(sDesmarcar1, (float) 0.4)); //key , defValue
        ibDesmarcar2.setAlpha(shp.getFloat(sDesmarcar2, (float) 0.4)); //key , defValue
    }

    public void desmarcar(ImageButton ibDesmarcar, String sDesmarcar) {
        SharedPreferences shp = getSharedPreferences(nameSP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();
        editor.remove(sDesmarcar);
        editor.commit();

        ibDesmarcar.setAlpha(shp.getFloat(sDesmarcar, (float) 0.4));
    }

    public void eliminarPreferencias(String nameSP) {
        SharedPreferences sp = getSharedPreferences(nameSP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        //Modificamos los valores de las preferencias al valor por defecto
        editor.clear();
        editor.commit();

        marcarBotones(nameSP);
    }

    //Ponemos la opacidad guardada en el SharedPreferences
    private void marcarBotones(String nameSP) {
        SharedPreferences sp = getSharedPreferences(nameSP, Context.MODE_PRIVATE);
        imbtn_verdadero1.setAlpha(sp.getFloat("opaV1", (float) 0.4)); //key , defValue
        imbtn_falso1.setAlpha(sp.getFloat("opaF1", (float) 0.4)); //key , defValue
        imbtn_inter1.setAlpha(sp.getFloat("opaI1", (float) 0.4)); //key , defValue

        imbtn_verdadero2.setAlpha(sp.getFloat("opaV2", (float) 0.4)); //key , defValue
        imbtn_falso2.setAlpha(sp.getFloat("opaF2", (float) 0.4)); //key , defValue
        imbtn_inter2.setAlpha(sp.getFloat("opaI2", (float) 0.4)); //key , defValue

        imbtn_verdadero3.setAlpha(sp.getFloat("opaV3", (float) 0.4)); //key , defValue
        imbtn_falso3.setAlpha(sp.getFloat("opaF3", (float) 0.4)); //key , defValue
        imbtn_inter3.setAlpha(sp.getFloat("opaI3", (float) 0.4)); //key , defValue

        imbtn_verdadero4.setAlpha(sp.getFloat("opaV4", (float) 0.4)); //key , defValue
        imbtn_falso4.setAlpha(sp.getFloat("opaF4", (float) 0.4)); //key , defValue
        imbtn_inter4.setAlpha(sp.getFloat("opaI4", (float) 0.4)); //key , defValue

        imbtn_verdadero5.setAlpha(sp.getFloat("opaV5", (float) 0.4)); //key , defValue
        imbtn_falso5.setAlpha(sp.getFloat("opaF5", (float) 0.4)); //key , defValue
        imbtn_inter5.setAlpha(sp.getFloat("opaI5", (float) 0.4)); //key , defValue

        imbtn_verdadero6.setAlpha(sp.getFloat("opaV6", (float) 0.4)); //key , defValue
        imbtn_falso6.setAlpha(sp.getFloat("opaF6", (float) 0.4)); //key , defValue
        imbtn_inter6.setAlpha(sp.getFloat("opaI6", (float) 0.4)); //key , defValue
    }

}