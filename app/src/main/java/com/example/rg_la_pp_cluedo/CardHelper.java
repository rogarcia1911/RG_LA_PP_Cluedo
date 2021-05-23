package com.example.rg_la_pp_cluedo;

public class CardHelper {
    public enum Types {
        Murder,
        Tool,
        Room
    }

    public enum CardReferences{
        Murder1(R.string.tvPers1, R.drawable.pj_amapola),
        Murder2(R.string.tvPers2, R.drawable.pj_blanco),
        Murder3(R.string.tvPers3, R.drawable.pj_celeste),
        Murder4(R.string.tvPers4, R.drawable.pj_mora),
        Murder5(R.string.tvPers5, R.drawable.pj_prado),
        Murder6(R.string.tvPers6, R.drawable.pj_rubio),

        Tool1(R.string.tvArma1, R.drawable.arma_candelabro),
        Tool2(R.string.tvArma2, R.drawable.arma_cuerda),
        Tool3(R.string.tvArma3, R.drawable.arma_herramienta),
        Tool4(R.string.tvArma4, R.drawable.arma_pistola),
        Tool5(R.string.tvArma5, R.drawable.arma_punial),
        Tool6(R.string.tvArma6, R.drawable.arma_tuberia),

        Room1(R.string.tvHab1, R.drawable.lugar_banio),
        Room2(R.string.tvHab2, R.drawable.lugar_comedor),
        Room3(R.string.tvHab3, R.drawable.lugar_dormitorio),
        Room4(R.string.tvHab4, R.drawable.lugar_estudio),
        Room5(R.string.tvHab5, R.drawable.lugar_garaje),
        Room6(R.string.tvHab6, R.drawable.lugar_patio);

        private int nameRef;
        private int imageRef;

        public int getNameRef() {
            return nameRef;
        }
        public int getImageRef() {
            return imageRef;
        }

        private CardReferences(int nameRef,int imageRef) {
            this.nameRef = nameRef;
        }
    }
}
