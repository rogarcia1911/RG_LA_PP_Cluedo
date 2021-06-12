package com.example.rg_la_pp_cluedo;

import java.io.Serializable;

public class Carta implements Serializable {

    private String nombre;
    private int imagen;
    private boolean culpable;

    public Carta(String nombre, int imagen, boolean culpable) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.culpable = culpable;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public void setCulpable(boolean culpable) {
        this.culpable = culpable;
    }

    public String getNombre() {
        return nombre;
    }

    public int getImagen() {
        return imagen;
    }

    public boolean isCulpable() {
        return culpable;
    }

}
