package com.example.rg_la_pp_cluedo.BBDD;

import java.util.ArrayList;

public class Player {

    private String userName;
    private String status;
    private String match;
    private Integer turn;
    private ArrayList<Integer> mineCards;


    public Player() {
        userName = "";
        match = "";

    }

    public Player(String userName, String status, String match, Integer turn, ArrayList<Integer> mineCards) {
        this.userName = userName;
        this.status = status;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTurn() {
        return turn;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }
}


