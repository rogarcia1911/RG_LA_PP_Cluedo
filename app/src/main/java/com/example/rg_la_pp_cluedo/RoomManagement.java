package com.example.rg_la_pp_cluedo;

import android.util.Log;

import com.example.rg_la_pp_cluedo.BBDD.Match;
import com.example.rg_la_pp_cluedo.BBDD.Player;
import com.example.rg_la_pp_cluedo.BBDD.Room;

import java.util.ArrayList;

public class RoomManagement {

    public  static void main(Object[] player1, Object[]player2) {
        // 0-userName 1-match 2-turn

        RoomMang roomMang = new RoomMang();
        Player player = new Player();

        PlayerThread player1T = new PlayerThread(roomMang,(Match)player1[1],(Integer)player1[2]);
        PlayerThread player2T = new PlayerThread(roomMang,(Match)player2[1],(Integer)player2[2]);
    }
}

class RoomMang {
    Integer turno = 0;
    Long endDate = 0L;
    ArrayList<Integer> PickedCards;

    synchronized void jugar(String s, int miTurno) {
        while (turno != miTurno) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.i("HiloPlayer", "Esperando...");
    }
    synchronized  void sospechar() {
        //recuperar pickedCards
        turno = (turno+1)%2;
        notifyAll();
    }

}

class PlayerThread extends Thread {
    RoomMang roomMang;
    Match match;
    int turno;

    public PlayerThread(RoomMang roomMang, Match match, int turno) {
        this.roomMang = roomMang;
        this.match = match;
        this.turno = turno;
    }

    public void run() {
        while(roomMang.endDate==0L){
            roomMang.jugar(match.getName(),turno);
            roomMang.sospechar();
        }
        Log.i("Fin partida Multi  :  ", String.valueOf(roomMang.endDate));
    }
}
