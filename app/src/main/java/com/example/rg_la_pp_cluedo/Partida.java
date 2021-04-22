package com.example.rg_la_pp_cluedo;

import java.lang.reflect.Array;

public class Partida {

    private int id;
    private Array cartas;
    private boolean enCurso;

    public Partida(int id, Array cartas, boolean enCurso) {
        this.id = id;
        this.cartas = cartas;
        this.enCurso = enCurso;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCartas(Array cartas) {
        this.cartas = cartas;
    }

    public void setEnCurso(boolean enCurso) {
        this.enCurso = enCurso;
    }

    public int getId() {
        return id;
    }

    public Array getCartas() {
        return cartas;
    }

    public boolean isEnCurso() {
        return enCurso;
    }
}
