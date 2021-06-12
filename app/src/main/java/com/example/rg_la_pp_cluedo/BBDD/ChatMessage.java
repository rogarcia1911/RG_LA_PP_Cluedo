package com.example.rg_la_pp_cluedo.BBDD;

public class ChatMessage {

    private String messagePlayer;
    private String messageText;
    private String messageTime;

    public ChatMessage() {

    }

    public ChatMessage(String messagePlayer, String messageText) {
        this.messagePlayer = messagePlayer;
        this.messageText = messageText;
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

}
