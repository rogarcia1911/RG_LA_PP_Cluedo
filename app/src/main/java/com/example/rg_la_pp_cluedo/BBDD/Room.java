package com.example.rg_la_pp_cluedo.BBDD;

import java.sql.Array;
import java.util.ArrayList;

public class Room {

    private String name;
    private Integer turn;
    private Array cardsSuspicious;
    private ArrayList players;
    private ArrayList<ChatMessage> Chat;

    public Room() {

    }


    public Room(String name, Integer turn, Array cardsSuspicious, ArrayList players) {
        this.name = name;
        this.turn = turn;
        this.cardsSuspicious = cardsSuspicious;
        this.players = players;
        this.Chat = new ArrayList<ChatMessage>();
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

    public ArrayList getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList players) {
        this.players = players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public ArrayList<ChatMessage> getChat() {
        return Chat;
    }

    public void setChat(ArrayList<ChatMessage> Chat) {
        this.Chat = Chat;
    }
}
