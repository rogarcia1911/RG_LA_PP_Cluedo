package com.example.rg_la_pp_cluedo.BBDD;

import java.util.ArrayList;

public class Room {

    private String name;
    private String turn;
    private String status;
    private Match match;
    private ArrayList<Integer> CardsPicked;
    private ArrayList<ChatMessage> Chat;


    public Room() {
        name = "";
        turn = "";
    }


    public Room(String name, String turn, String status, Match match, ArrayList<Integer> CardsPicked,
                ArrayList<Player> Players, ArrayList<ChatMessage> Chat) {
        this.name = name;
        this.turn = turn;
        this.status = status;
        this.match=match;
        this.CardsPicked = CardsPicked;
        this.Chat = Chat;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public ArrayList<ChatMessage> getChat() {
        return Chat;
    }

    public void setChat(ArrayList<ChatMessage> Chat) {
        this.Chat = Chat;
    }

    public ArrayList<Integer> getCardsPicked() {
        return CardsPicked;
    }

    public void setCardsPicked(ArrayList<Integer> CardsPicked) {
        this.CardsPicked = CardsPicked;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
