package com.example.rg_la_pp_cluedo.BBDD;

public class Match {


     /**
      * Nombre de la partida (nombre del jugador)
      */
     private String name;
     /**
      * Número de partida
      */
     private Integer num;
     /**
      * Tiempo de incio de partida
      */
     private Long beginningDate;
     /**
      * Tiempo de fin de partida
      */
     private Long endingDate;
     /**
      * Resultado de la partida
      */
     private Boolean resultGame;
     /**
      * Modo de juego (Solo/Multi)
      */
     private Boolean isSolo;
     /**
      * Dificultad de partida de modo Solo
      */
     private String difficulty;
     /**
      * Las 3 cartas correctas (culpables del asesinato)
      */
     private Card[] murderCards;
     //private Long matchTime;   //Calcular tiempo jugado /Solo/ (fin-inico-(pausa.fin-pausa.inicio))

     public Match(){
          name = null;
          num = null;
          beginningDate = null;
          endingDate = null;
          resultGame = null;
          isSolo = null;
          difficulty = null;
     }

     public Match(String name, Integer num, Long beginningDate, Long endingDate, Boolean resultGame, Boolean isSolo,
                  String difficulty, Card[] murderCards) {
          this.name = name;
          this.num = num;
          this.beginningDate = beginningDate;
          this.endingDate = endingDate;
          this.resultGame = resultGame;
          this.isSolo = isSolo;
          this.difficulty = difficulty;
          this.murderCards = murderCards;
     }

     public String getName() {
          return name;
     }

     public void setName(String name) {
          this.name = name;
     }

     public Integer getNum() {
          return num;
     }

     public void setNum(Integer num) {
          this.num = num;
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

     public Boolean getResultGame() {
          return resultGame;
     }

     public Boolean getIsSolo() {
          return isSolo;
     }

     public void setIsSolo(Boolean isSolo) {
          this.isSolo = isSolo;
     }

     public void setResultGame(Boolean resultGame) {
          this.resultGame = resultGame;
     }

     public String getDifficulty() {
          return difficulty;
     }

     public void setDifficulty(String difficulty) {
          this.difficulty = difficulty;
     }

     public Card[] getMurderCards() {
          return murderCards;
     }

     public void setMurderCards(Card[] murderCards) {
          this.murderCards = murderCards;
     }

     @Override
     public String toString() {
          return "Match{" +
                  "  name=" + (name==null ? null : "'" + name + "'" )  +
                  ", num=" + num +
                  ", beginningDate=" + beginningDate +
                  ", endingDate=" + endingDate +
                  ", resultGame=" + resultGame +
                  ", solo='" + isSolo + '\'' +
                  ", difficulty='" + difficulty + "'" +
                  ", Murdered cards='" + murderCards + "'" +
                  '}';
     }
}
