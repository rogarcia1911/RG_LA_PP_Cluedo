package com.example.rg_la_pp_cluedo.BBDD;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Room {

    private String name;
    private Integer turn;
    private Card[] CardsPicked;
    private Array Players;
    private Array Chat;


    public Room() {

    }


    public Room(String name, Integer turn, Card[] CardsPicked, Array Players, Array Chat) {
        this.name = name;
        this.turn = turn;
        this.CardsPicked = CardsPicked;
        this.Players = Players;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTurn() {
        return turn;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }

    public Array getPlayers() {
        return Players;
    }

    public void setPlayers(Array Players) {
        this.Players = Players;
    }

    public Array getChat() {
        return Chat;
    }

    public void setChat(Array Chat) {
        this.Chat = Chat;
    }
}
