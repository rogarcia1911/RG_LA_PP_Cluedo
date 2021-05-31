package com.example.rg_la_pp_cluedo;

public class MatchHelper {

    public enum Mode {
        SOLO,
        MULTI
    }

    public enum Difficulty {
        EASY(10),
        MEDIUM(8),
        HARD(5);

        private int cont;

        public int getCont(){
            return cont;
        }

        private Difficulty(int cont) {
            this.cont = cont;
        }
    }

    public enum Cards {
        // D - Default
        D0(0, R.drawable.carta_interrogante, R.string.tvCardDefault),
        // M - Murders
        M1(10, R.drawable.pj_amapola, R.string.tvPers1),
        M2(11, R.drawable.pj_blanco, R.string.tvPers2),
        M3(12, R.drawable.pj_celeste, R.string.tvPers3),
        M4(13, R.drawable.pj_mora, R.string.tvPers4),
        M5(14, R.drawable.pj_prado, R.string.tvPers5),
        M6(15, R.drawable.pj_rubio, R.string.tvPers6),
        // T - Tools
        T1(20, R.drawable.arma_candelabro, R.string.tvArma1),
        T2(21, R.drawable.arma_cuerda, R.string.tvArma2),
        T3(22, R.drawable.arma_herramienta, R.string.tvArma3),
        T4(23, R.drawable.arma_pistola, R.string.tvArma4),
        T5(24, R.drawable.arma_punial, R.string.tvArma5),
        T6(25, R.drawable.arma_tuberia, R.string.tvArma6),
        // R - Room
        R1(30, R.drawable.lugar_banio, R.string.tvHab1),
        R2(31, R.drawable.lugar_comedor, R.string.tvHab2),
        R3(32, R.drawable.lugar_dormitorio, R.string.tvHab3),
        R4(33, R.drawable.lugar_estudio, R.string.tvHab4),
        R5(34, R.drawable.lugar_garaje, R.string.tvHab5),
        R6(35, R.drawable.lugar_patio, R.string.tvHab6);

        private int ref;
        private int img;
        private int text;

        private int nCardsByType = 6;

        public int getRef(){
            return ref;
        }
        public int getImg(){
            return img;
        }
        public int getText(){
            return text;
        }
        public int getNCardsByType() {
            return nCardsByType;
        }

        private Cards(int ref, int img, int text) {
            this.ref = ref;
            this.img = img;
            this.text = text;
        }
    }
}
