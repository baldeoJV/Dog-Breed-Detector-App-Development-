package com.example.appdevy3t3;

public class DogModel {
    String dogName;
    int image;

    public DogModel(String dogName, int image) {
        this.dogName = dogName;
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public String getDogName() {
        return dogName;
    }
}
