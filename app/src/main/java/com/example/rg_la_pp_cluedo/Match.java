package com.example.rg_la_pp_cluedo;

import java.sql.Array;
import java.sql.Timestamp;

public class Match {

     private Integer matchId;
     private Long beginningDate;
     private Long endingDate;
     private String mode;
     private Boolean resultGame;
     private Array matchCards;
     private Integer playerNum;
     //Dificultad   ??   JSON?

     public Match(){

     }

     public Match(Integer matchId, Long beginningDate, Long endingDate, String mode, Boolean resultGame, Array matchCards, Integer playerNum) {
          this.matchId = matchId;
          this.beginningDate = beginningDate;
          this.endingDate = endingDate;
          this.mode = mode;
          this.resultGame = resultGame;
          this.matchCards = matchCards;
          this.playerNum = playerNum;
     }

     public Integer getMatchId() {
          return matchId;
     }

     public void setMatchId(Integer matchId) {
          this.matchId = matchId;
     }

     public Long getBeginningDate() {
          return beginningDate;
     }

     public void setBeginningDate(Long beginningDate) {
          this.beginningDate = beginningDate;
     }

     public Long getEndingDate() {
          return endingDate;
     }

     public void setEndingDate(Long endingDate) {
          this.endingDate = endingDate;
     }

     public String getMode() {
          return mode;
     }

     public void setMode(String mode) {
          this.mode = mode;
     }

     public Boolean getResultGame() {
          return resultGame;
     }

     public void setResultGame(Boolean resultGame) {
          this.resultGame = resultGame;
     }

     public Array getMatchCards() {
          return matchCards;
     }

     public void setMatchCards(Array matchCards) {
          this.matchCards = matchCards;
     }

     public Integer getPlayerNum() {
          return playerNum;
     }

     public void setPlayerNum(Integer playerNum) {
          this.playerNum = playerNum;
     }

     @Override
     public String toString() {
          return "Match{" +
                  "  matchId=" + matchId +
                  ", beginningDate=" + beginningDate +
                  ", endingDate=" + endingDate +
                  ", mode='" + mode + '\'' +
                  ", resultGame=" + resultGame +
                  ", gameCards=" + matchCards +
                  ", playerNum=" + playerNum +
                  '}';
     }
}
