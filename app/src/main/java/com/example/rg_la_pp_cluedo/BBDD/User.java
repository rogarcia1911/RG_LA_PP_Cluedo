package com.example.rg_la_pp_cluedo.BBDD;

import com.example.rg_la_pp_cluedo.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class User {

    private String name;
    private Integer numSoloMatchs;
    private Integer numMultiMatchs;
    private Integer points;

    public User(){
        this.name = null;
        this.numSoloMatchs = null;
        this.numMultiMatchs = null;
        this.points = null;
    }

    public User(String name, Integer numSoloMatchs, Integer numMultiMatchs, Integer points) {
        this.name = name;
        this.numSoloMatchs = numSoloMatchs;
        this.numMultiMatchs = numMultiMatchs;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumSoloMatchs() {
        return numSoloMatchs;
    }

    public void setNumSoloMatchs(Integer numSoloMatchs) {
        this.numSoloMatchs = numSoloMatchs;
    }

    public Integer getNumMultiMatchs() {
        return numMultiMatchs;
    }

    public void setNumMultiMatchs(Integer numMultiMatchs) {
        this.numMultiMatchs = numMultiMatchs;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "User{" +
                "  name='" + name + "'"   +
                ", numMatch=" + numSoloMatchs +
                ", numMatch=" + numMultiMatchs +
                ", points='" + points + "'" +
                '}';
    }
}
