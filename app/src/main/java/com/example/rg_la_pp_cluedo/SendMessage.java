package com.example.rg_la_pp_cluedo;

import com.example.rg_la_pp_cluedo.BBDD.ChatMessage;

import java.util.Map;

public class SendMessage extends ChatMessage {
    private Map hour;

    public SendMessage() {
    }

    public SendMessage(Map hour) {
        this.hour = hour;
    }



    public SendMessage(String messagePlayer, String messageText, Integer massageImgage, Map hour) {
        super(messagePlayer, messageText,massageImgage);
        this.hour = hour;
    }

    public Map getHour() {
        return hour;
    }

    public void setHour(Map hour) {
        this.hour = hour;
    }
}
