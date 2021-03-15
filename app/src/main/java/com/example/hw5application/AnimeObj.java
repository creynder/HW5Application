package com.example.hw5application;

public class AnimeObj {
    private String title;
    private String imageURL;
    private double score;
    private String rating;
    private boolean airing;
    private String descrip;
    private String type;
    private int malID;

    //constructor
    public AnimeObj(String title, String imageURL, double score, String rating, boolean airing, String descrip, String type){
        this.title = title;
        this.imageURL = imageURL;
        this.score = score;
        this.rating = rating;
        this.airing = airing;
        this.descrip = descrip;
        this.type = type;
    }

    //get functions
    public String getTitle() {
        return title;
    }
    public String getImageURL() {
        return imageURL;
    }
    public double getScore() {
        return score;
    }
    public String getRating() {
        return rating;
    }
    public boolean isAiring() {
        return airing;
    }
    public String getDescrip() {
        return descrip;
    }
    public String getType() {
        return type;
    }
    public int getMalID() {return malID;}


    //set functions
    public void setTitle(String title) {
        this.title = title;
    }
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    public void setScore(double score) {
        this.score = score;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }
    public void setAiring(boolean airing) {
        this.airing = airing;
    }
    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setMalID(int malID) {this.malID = malID;}

}
