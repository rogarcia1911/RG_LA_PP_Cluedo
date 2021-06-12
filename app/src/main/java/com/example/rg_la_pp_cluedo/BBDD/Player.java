package com.example.rg_la_pp_cluedo.BBDD;

import java.util.ArrayList;

public class Player {

    private String userName;
    private ArrayList<Integer> mineCards;


    public Player() {
        userName = "";

    }

    public Player(String userName, String status, String match, Integer turn, ArrayList<Integer> mineCards) {
        this.userName = userName;
        this.mineCards = mineCards;
    }

    @Override
    public String toString() {
        return "Player: " +
                "userName='" + userName + '\'' +
                ", mineCards=" + mineCards +
                '.';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}


