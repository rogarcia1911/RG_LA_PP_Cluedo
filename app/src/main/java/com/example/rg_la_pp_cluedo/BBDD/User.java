package com.example.rg_la_pp_cluedo.BBDD;

public class User {

    private String name;
    private String email;
    private Integer numSoloMatchs;
    private Integer numMultiMatchs;
    private Integer points;

    public User(){
        this.name = "";
        this.email = "";
        this.numSoloMatchs = 0;
        this.numMultiMatchs = 0;
        this.points = 0;
    }

    public User(String name, String email, Integer numSoloMatchs, Integer numMultiMatchs, Integer points) {
        this.name = name;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
