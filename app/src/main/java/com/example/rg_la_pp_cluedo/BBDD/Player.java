package com.example.rg_la_pp_cluedo.BBDD;

public class Player {

    private Integer playerId;
    private String userName;
    private String email;
    private String password;
    private Integer score;
    private String country;
    private Integer image;  //Referencia del integer a la imagen
    @Deprecated
    private Boolean login;


    public Player() {
    }

    public Player(Integer playerId, String userName, String email, String password, Integer score, String country, Integer image, Boolean login) {
        this.playerId = playerId;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.score = score;
        this.country = country;
        this.image = image;
        this.login = login;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public Boolean getLogin() {
        return login;
    }

    public void setLogin(Boolean login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "Player: " +
                "playerId=" + playerId +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", score=" + score +
                ", country='" + country + '\'' +
                ", image=" + image +
                ", login=" + login +
                '.';
    }
}


