package com.example.rg_la_pp_cluedo.BBDD;

import java.util.ArrayList;

public class Room {
//TODO: revisar los atrbutos, constructor y métodos setter y getter
    String name;
    private String status;
    private Match match;
    private String player1;
    private String player2;
    private ArrayList<Integer> cardsPicked;
    //private ArrayList<ChatMessage> Chat;


    public Room() {
        name = "";
        status = "";
    }


    public Room(String name, String turn, String status, String player1, String player2, Match match, ArrayList<Integer> cardsPicked,
                ArrayList<Player> Players, ArrayList<ChatMessage> Chat) {
        this.name=name;
        this.status = status;
        this.match = match;
        this.player1 = player1;
        this.player2 = player2;
        this.cardsPicked = cardsPicked;
        //this.Chat = Chat;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    /*public ArrayList<ChatMessage> getChat() {
        return Chat;
    }

    public void setChat(ArrayList<ChatMessage> Chat) {
        this.Chat = Chat;
    }*/

    public ArrayList<Integer> getCardsPicked() {
        return cardsPicked;
    }

    public void setCardsPicked(ArrayList<Integer> cardsPicked) {
        this.cardsPicked = cardsPicked;
    }

    public String getStatus() {
        return status;
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
