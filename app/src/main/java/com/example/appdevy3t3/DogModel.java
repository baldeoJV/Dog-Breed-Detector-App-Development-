package com.example.appdevy3t3;

import android.os.Parcel;
import android.os.Parcelable;

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
//
//package com.example.appdevy3t3;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//public class DogModel implements Parcelable {
//
//    private String dog_name;
//    private String imageUrl;
//
//    private String bredFor;
//    private String breedGroup;
//    private String lifeSpan;
//    private String temperament;
//    private String weightMin;
//    private String weightMax;
//    private String heightMin;
//    private String heightMax;
//    private String expectancyMin;
//    private String expectancyMax;
//    private String grooming;
//    private String shedding;
//    private String energyLevel;
//    private String trainability;
//    private String demeanor;
//
//    // Constructors
//    public DogModel() {
//    }
//
//    public DogModel(String dog_name, String imageUrl, String bredFor, String breedGroup, String lifeSpan,
//                    String temperament, String weightMin, String weightMax, String heightMin, String heightMax,
//                    String expectancyMin, String expectancyMax, String grooming, String shedding,
//                    String energyLevel, String trainability, String demeanor) {
//
//        this.dog_name = dog_name;
//        this.imageUrl = imageUrl;
//        this.bredFor = bredFor;
//        this.breedGroup = breedGroup;
//        this.lifeSpan = lifeSpan;
//        this.temperament = temperament;
//        this.weightMin = weightMin;
//        this.weightMax = weightMax;
//        this.heightMin = heightMin;
//        this.heightMax = heightMax;
//        this.expectancyMin = expectancyMin;
//        this.expectancyMax = expectancyMax;
//        this.grooming = grooming;
//        this.shedding = shedding;
//        this.energyLevel = energyLevel;
//        this.trainability = trainability;
//        this.demeanor = demeanor;
//    }
//
//    // Parcelable implementation
//    protected DogModel(Parcel in) {
//        dog_name = in.readString();
//        imageUrl = in.readString();
//        bredFor = in.readString();
//        breedGroup = in.readString();
//        lifeSpan = in.readString();
//        temperament = in.readString();
//        weightMin = in.readString();
//        weightMax = in.readString();
//        heightMin = in.readString();
//        heightMax = in.readString();
//        expectancyMin = in.readString();
//        expectancyMax = in.readString();
//        grooming = in.readString();
//        shedding = in.readString();
//        energyLevel = in.readString();
//        trainability = in.readString();
//        demeanor = in.readString();
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(dog_name);
//        dest.writeString(imageUrl);
//        dest.writeString(bredFor);
//        dest.writeString(breedGroup);
//        dest.writeString(lifeSpan);
//        dest.writeString(temperament);
//        dest.writeString(weightMin);
//        dest.writeString(weightMax);
//        dest.writeString(heightMin);
//        dest.writeString(heightMax);
//        dest.writeString(expectancyMin);
//        dest.writeString(expectancyMax);
//        dest.writeString(grooming);
//        dest.writeString(shedding);
//        dest.writeString(energyLevel);
//        dest.writeString(trainability);
//        dest.writeString(demeanor);
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    public static final Creator<DogModel> CREATOR = new Creator<DogModel>() {
//        @Override
//        public DogModel createFromParcel(Parcel in) {
//            return new DogModel(in);
//        }
//
//        @Override
//        public DogModel[] newArray(int size) {
//            return new DogModel[size];
//        }
//    };
//
//    // Getters and Setters
//    public String getDog_name() { return dog_name; }
//    public void setDog_name(String dog_name) { this.dog_name = dog_name; }
//
//    public String getImageUrl() { return imageUrl; }
//    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
//
//    public String getBredFor() { return bredFor; }
//    public void setBredFor(String bredFor) { this.bredFor = bredFor; }
//
//    public String getBreedGroup() { return breedGroup; }
//    public void setBreedGroup(String breedGroup) { this.breedGroup = breedGroup; }
//
//    public String getLifeSpan() { return lifeSpan; }
//    public void setLifeSpan(String lifeSpan) { this.lifeSpan = lifeSpan; }
//
//    public String getTemperament() { return temperament; }
//    public void setTemperament(String temperament) { this.temperament = temperament; }
//
//    public String getWeightMin() { return weightMin; }
//    public void setWeightMin(String weightMin) { this.weightMin = weightMin; }
//
//    public String getWeightMax() { return weightMax; }
//    public void setWeightMax(String weightMax) { this.weightMax = weightMax; }
//
//    public String getHeightMin() { return heightMin; }
//    public void setHeightMin(String heightMin) { this.heightMin = heightMin; }
//
//    public String getHeightMax() { return heightMax; }
//    public void setHeightMax(String heightMax) { this.heightMax = heightMax; }
//
//    public String getExpectancyMin() { return expectancyMin; }
//    public void setExpectancyMin(String expectancyMin) { this.expectancyMin = expectancyMin; }
//
//    public String getExpectancyMax() { return expectancyMax; }
//    public void setExpectancyMax(String expectancyMax) { this.expectancyMax = expectancyMax; }
//
//    public String getGrooming() { return grooming; }
//    public void setGrooming(String grooming) { this.grooming = grooming; }
//
//    public String getShedding() { return shedding; }
//    public void setShedding(String shedding) { this.shedding = shedding; }
//
//    public String getEnergyLevel() { return energyLevel; }
//    public void setEnergyLevel(String energyLevel) { this.energyLevel = energyLevel; }
//
//    public String getTrainability() { return trainability; }
//    public void setTrainability(String trainability) { this.trainability = trainability; }
//
//    public String getDemeanor() { return demeanor; }
//    public void setDemeanor(String demeanor) { this.demeanor = demeanor; }
//}
