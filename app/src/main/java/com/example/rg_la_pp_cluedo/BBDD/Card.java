package com.example.rg_la_pp_cluedo.BBDD;

public class Card {

    /**
     * Identificador de la carta
     */
    private Integer cardId;
    /**
     * Tipo de carta (Person|Room|Tool)
     */
    private String type;
    /**
     * Nombre de la carta
     */
    private String cardName;
    /**
     * Referencia de recursos
     */
    private Integer image;  //id of resources

    public Card() {
    }

    public Card(Integer cardId, String type, String cardName, Integer image) {
        this.cardId = cardId;
        this.type = type;
        this.cardName = cardName;
        this.image = image;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardId=" + cardId +
                ", type='" + type + '\'' +
                ", cardName='" + cardName + '\'' +
                ", image=" + image +
                '}';
    }
}
