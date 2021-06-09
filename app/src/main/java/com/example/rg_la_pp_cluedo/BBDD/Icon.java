package com.example.rg_la_pp_cluedo.BBDD;

import com.example.rg_la_pp_cluedo.R;

import java.util.ArrayList;

public class Icon {

    private Boolean selected;
    private Integer image;

    public Icon(){
        selected = null;
        image = null;
    }

    public Icon(Boolean selected, Integer image) {
        this.selected = selected;
        this.image = image;
    }

    public Boolean isSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }
}
