package com.example.rg_la_pp_cluedo.BBDD;

import java.util.ArrayList;

public class Room {

    private String name;
    private Integer turn;
    private Long endDate;
    private ArrayList<Player> Players;
    private ArrayList<Integer> CardsPicked;
    private ArrayList<ChatMessage> Chat;


    public Room() {
        name = "";
        turn = 0;
        endDate = 0L;
    }


    public Room(String name, Integer turn, Long endDate, ArrayList<Integer> CardsPicked,
                ArrayList<Player> Players, ArrayList<ChatMessage> Chat) {
        this.name = name;
        this.turn = turn;
        this.endDate = endDate;
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

    public ArrayList<Player> getPlayers() {
        return Players;
    }

    public void setPlayers(ArrayList<Player> Players) {
        this.Players = Players;
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

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long ended) {
        this.endDate = endDate;
    }
}
