package com.example.rg_la_pp_cluedo;

import com.example.rg_la_pp_cluedo.BBDD.ChatMessage;

public class RecieveMessage extends ChatMessage {
    private Long hour;

    private RecieveMessage(){
    }

    public RecieveMessage(Long hour) {
        this.hour = hour;
    }

    public RecieveMessage(String messagePlayer, String messageText, Long hour) {
        super(messagePlayer, messageText);
        this.hour = hour;
    }

    public Long getHour() {
        return hour;
    }

    public void setHour(Long hour) {
        this.hour = hour;
    }
}
