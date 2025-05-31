package com.example.appdevy3t3;

public class DogModel {

    private String dog_name;

    private String imageUrl;

    //CONSTRUCTORS

    public DogModel(String dog_name, String imageUrl) {
        this.dog_name = dog_name;
        this.imageUrl = imageUrl;
    }

    public DogModel() {
    }

    //GETTERS SETTERS

    public String getDog_name() {
        return dog_name;
    }

    public void setDog_name(String dog_name) {
        this.dog_name = dog_name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
