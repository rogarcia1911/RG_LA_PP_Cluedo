package com.example.rg_la_pp_cluedo.BBDD;

import java.util.ArrayList;

public class Player {

    private String userName;
    private String match;
    private Integer turn;
    private ArrayList<Integer> mineCards;


    public Player() {
        userName = "";
        match = "";

    }

    public Player(String userName, String match, Integer turn, ArrayList<Integer> mineCards) {
        this.userName = userName;
        this.match = match;
        this.turn = turn;
        this.mineCards = mineCards;
    }

    @Override
    public String toString() {
        return "Player: " +
                "userName='" + userName + '\'' +
                ", match=" + match +
                ", turn=" + turn +
                ", mineCards=" + mineCards +
                '.';
    }
}


