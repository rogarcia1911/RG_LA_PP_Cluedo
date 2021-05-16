package com.example.rg_la_pp_cluedo;

import org.json.JSONObject;

import java.sql.Array;

public class MatchHistory {

    private Integer matchId;
    private Array players;

    public MatchHistory() {
    }


    public MatchHistory(Integer matchId, Array players) {
        this.matchId = matchId;
        this.players = players;
    }

    public Integer getMatchId() {
        return matchId;
    }

    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }

    public Array getPlayers() {
        return players;
    }

    public void setPlayers(Array players) {
        this.players = players;
    }



}
