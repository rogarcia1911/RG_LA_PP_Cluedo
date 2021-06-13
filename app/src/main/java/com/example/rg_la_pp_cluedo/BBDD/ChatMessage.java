package com.example.rg_la_pp_cluedo.BBDD;

public class ChatMessage {

    private String messagePlayer;
    private String messageText;
    private Integer playerimage;

    public ChatMessage() {

    }

    public ChatMessage(String messagePlayer, String messageText, Integer playerimage) {
        this.messagePlayer = messagePlayer;
        this.messageText = messageText;
        this.playerimage = playerimage;
    }

    public String getMessagePlayer() {
        return messagePlayer;
    }

    public void setMessagePlayer(String messagePlayer) {
        this.messagePlayer = messagePlayer;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Integer getPlayerimage() {
        return playerimage;
    }

    public void setPlayerimage(Integer playerimage) {
        this.playerimage = playerimage;
    }

}
