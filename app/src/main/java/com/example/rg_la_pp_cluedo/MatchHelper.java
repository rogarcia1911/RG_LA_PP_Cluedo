package com.example.rg_la_pp_cluedo;

public class MatchHelper {

    public enum mode {
        Solo,
        Multi
    }

    public enum difficulty {
        Easy(10),
        Medium(8),
        Hard(5);

        private int cont;

        public int getCont(){
            return cont;
        }

        private difficulty(int cont) {
            this.cont = cont;
        }
    }
}
