package com.example.rg_la_pp_cluedo.BBDD;

import com.example.rg_la_pp_cluedo.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class User {

    private String name;
    private Integer numMatchs;
    private Integer points;

    public User(){

    }

    public User(String name, Integer numMatchs, Integer points) {
        this.name = name;
        this.numMatchs = numMatchs;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumMatchs() {
        return numMatchs;
    }

    public void setNumMatchs(Integer numMatchs) {
        this.numMatchs = numMatchs;
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
                ", numMatch=" + numMatchs +
                ", points='" + points + "'" +
                '}';
    }
}
