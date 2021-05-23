package com.example.rg_la_pp_cluedo.BBDD;

public class ChatMessage {

    private String messagePlayer;
    private String messageText;
    private Long messageTime;

    public ChatMessage() {

    }

    public ChatMessage(String messagePlayer, String messageText) {
        this.messagePlayer = messagePlayer;
        this.messageText = messageText;
        this.messageTime = System.currentTimeMillis();
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

    public Long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Long messageTime) {
        this.messageTime = messageTime;
    }
}
