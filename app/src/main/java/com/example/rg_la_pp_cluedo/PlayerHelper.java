package com.example.rg_la_pp_cluedo;

public class PlayerHelper {

    public enum Status {
        Playing("R.string.msjPlaying"),
        WaitForAnotherPlayer("R.string.msjWaitAnotherPlayer"),
        WaitYourTurn("R.string.msjWaitYourTurn");

        private String statusText;
        public String getStatusText() {
            return statusText;
        }

        Status(String statusText) {
            this.statusText = statusText;
        }
    }
}
