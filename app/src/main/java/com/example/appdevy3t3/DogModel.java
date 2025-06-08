package com.example.appdevy3t3;

import android.os.Parcel;
import android.os.Parcelable;

public class DogModel {

    private String dog_name;

    private String imageUrl;

    // information of dog
    private String bredFor; // bred_for
    private String breedGroup; // breed_groupp
    private String lifespan; // life_span
    private String temperament; // temperament
    private String weightRange; // (min_weight (kg) - max_weight (kg))
    private String heightRange; // (min_height (cm) - max_height (cm))
    private String expectancyRange; // (min_expectancy (years) - max_expectancy (years))
    private String grooming; // grooming_frequency_category
    private String shedding; // shedding_category
    private String energyLevel; // energy_level_category
    private String trainability; // trainability_category
    private String demeanor; // demeanor_category
    private String description; // description

    //CONSTRUCTORS


    public DogModel(String dog_name, String imageUrl,
                    String bredFor, String breedGroup, String lifespan, String temperament,
                    String weightRange, String heightRange, String expectancyRange, String grooming,
                    String shedding, String energyLevel, String trainability, String demeanor,
                    String description) {

        this.dog_name = dog_name;
        this.imageUrl = imageUrl;

        this.bredFor = bredFor;
        this.breedGroup = breedGroup;
        this.lifespan = lifespan;
        this.temperament = temperament;
        this.weightRange = weightRange;
        this.heightRange = heightRange;
        this.expectancyRange = expectancyRange;
        this.grooming = grooming;
        this.shedding = shedding;
        this.energyLevel = energyLevel;
        this.trainability = trainability;
        this.demeanor = demeanor;
        this.description = description;
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

    public String getBredFor() {
        return bredFor;
    }

    public void setBredFor(String bredFor) {
        this.bredFor = bredFor;
    }

    public String getBreedGroup() {
        return breedGroup;
    }

    public void setBreedGroup(String breedGroup) {
        this.breedGroup = breedGroup;
    }

    public String getLifespan() {
        return lifespan;
    }

    public void setLifespan(String lifespan) {
        this.lifespan = lifespan;
    }

    public String getTemperament() {
        return temperament;
    }

    public void setTemperament(String temperament) {
        this.temperament = temperament;
    }

    public String getWeightRange() {
        return weightRange;
    }

    public void setWeightRange(String weightRange) {
        this.weightRange = weightRange;
    }

    public String getHeightRange() {
        return heightRange;
    }

    public void setHeightRange(String heightRange) {
        this.heightRange = heightRange;
    }

    public String getExpectancyRange() {
        return expectancyRange;
    }

    public void setExpectancyRange(String expectancyRange) {
        this.expectancyRange = expectancyRange;
    }

    public String getGrooming() {
        return grooming;
    }

    public void setGrooming(String grooming) {
        this.grooming = grooming;
    }

    public String getShedding() {
        return shedding;
    }

    public void setShedding(String shedding) {
        this.shedding = shedding;
    }

    public String getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(String energyLevel) {
        this.energyLevel = energyLevel;
    }

    public String getTrainability() {
        return trainability;
    }

    public void setTrainability(String trainability) {
        this.trainability = trainability;
    }

    public String getDemeanor() {
        return demeanor;
    }

    public void setDemeanor(String demeanor) {
        this.demeanor = demeanor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}