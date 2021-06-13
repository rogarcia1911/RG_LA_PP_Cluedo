package com.example.rg_la_pp_cluedo.BBDD;

import java.lang.reflect.Array;
import java.util.ArrayList;

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
     private ArrayList<Integer> murderCards;
     /**
      * Contador de la partida en modo Solo
      */
     private Integer cont;

     public Match(){
          name = "";
          num = 0;
          beginningDate = 0L;
          endingDate = 0L;
          resultGame = null;
          isSolo = null;
          difficulty = null;
          cont = 0;
     }


     /**
      * Constructor
      * @param name
      * @param num
      * @param beginningDate
      * @param endingDate
      * @param resultGame
      * @param isSolo
      * @param difficulty
      * @param murderCards
      * @param cont
      * @param roomName
      */
     public Match(String name, Integer num, Long beginningDate, Long endingDate, Boolean resultGame, Boolean isSolo,
                  String difficulty, ArrayList<Integer> murderCards, Integer cont, ArrayList<Integer> incorrectCards,
                  String roomName) {
          this.name = name;
          this.num = num;
          this.beginningDate = beginningDate;
          this.endingDate = endingDate;
          this.resultGame = resultGame;
          this.isSolo = isSolo;
          this.difficulty = difficulty;
          this.cont = cont;
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

     public void setResultGame(Boolean resultGame) {
          this.resultGame = resultGame;
     }

     public Boolean getIsSolo() {
          return isSolo;
     }

     public void setIsSolo(Boolean isSolo) {
          this.isSolo = isSolo;
     }

     public String getDifficulty() {
          return difficulty;
     }

     public void setDifficulty(String difficulty) {
          this.difficulty = difficulty;
     }

     public void setCont(Integer cont) {
          this.cont = cont;
     }

     public Integer getCont() {
          return cont;
     }

     public void setMurderCards(ArrayList<Integer> murderCards) {
          this.murderCards = murderCards;
     }

     public ArrayList<Integer> getMurderCards() {
          return murderCards;
     }

     @Override
     public String toString() {
          return "Match{" +
                  "  name='" + name + "'"  +
                  ", num=" + num +
                  ", beginningDate=" + beginningDate +
                  ", endingDate=" + endingDate +
                  ", resultGame=" + resultGame +
                  ", solo='" + isSolo + '\'' +
                  ", difficulty='" + difficulty + "'" +
                  ", murderCards=" + murderCards +
                  '}';
     }
}
