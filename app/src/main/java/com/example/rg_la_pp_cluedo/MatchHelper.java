package com.example.rg_la_pp_cluedo;

public class MatchHelper {

    public enum Mode {
        SOLO(true),
        MULTI(false);

        public static String getTextByB(boolean b){
            if (b)
                return SOLO.name();
            else
                return MULTI.name();
        }

        private boolean b;
        public boolean getB() {
            return b;
        }

        Mode(boolean b) {
            this.b = b;
        }
    }

    public enum Difficulty {
        EASY(10),
        MEDIUM(8),
        HARD(5);

        public int cont;

        public static int getContByName(String name) {
            if (name.equals(EASY.name()))
                return EASY.cont;
            if (name.equals(MEDIUM.name()))
                return MEDIUM.cont;
            if (name.equals(HARD.name()))
                return HARD.cont;
            return 0;
        }

        Difficulty(int cont) {
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
//TODO: Repasar codigo
        private int ref;
        private int img;
        private int text;

        public static int nCardsByType = 6;

        public int getRef(){
            return ref;
        }
        public int getImg(){
            return img;
        }
        public int getText(){
            return text;
        }

        public static int getImgByRef(int ref){
            switch (ref){
                // M - Murders
                case 10:
                    return M1.img;
                case 11:
                    return M2.img;
                case 12:
                    return M3.img;
                case 13:
                    return M4.img;
                case 14:
                    return M5.img;
                case 15:
                    return M6.img;
                // T - Tools
                case 20:
                    return T1.img;
                case 21:
                    return T2.img;
                case 22:
                    return T3.img;
                case 23:
                    return T4.img;
                case 24:
                    return T5.img;
                case 25:
                    return T6.img;
                // R - Room
                case 30:
                    return R1.img;
                case 31:
                    return R2.img;
                case 32:
                    return R3.img;
                case 33:
                    return R4.img;
                case 34:
                    return R5.img;
                case 35:
                    return R6.img;
                // D - Default
                default:
                    return D0.img;
            }
        }

        private Cards(int ref, int img, int text) {
            this.ref = ref;
            this.img = img;
            this.text = text;
        }
    }
}
