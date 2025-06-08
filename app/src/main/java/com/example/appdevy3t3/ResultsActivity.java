package com.example.appdevy3t3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ResultsActivity extends AppCompatActivity {

    ImageView dogImage;
    TextView dogName, bredFor, breedGroup, lifespan, temperament,
            weightRange, heightRange, expectancyRange,
            grooming, shedding, energyLevel, trainability, demeanor, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_library); // Make sure this layout exists

        // Initialize your views
        dogImage = findViewById(R.id.dogImage);
        dogName = findViewById(R.id.dogName);
        bredFor = findViewById(R.id.bredFor);
        breedGroup = findViewById(R.id.breedGroup);
        lifespan = findViewById(R.id.lifeSpan);
        temperament = findViewById(R.id.temperament);
        weightRange = findViewById(R.id.weightRange);
        heightRange = findViewById(R.id.heightRange);
        expectancyRange = findViewById(R.id.expectancyRange);
        grooming = findViewById(R.id.grooming);
        shedding = findViewById(R.id.shedding);
        energyLevel = findViewById(R.id.energyLevel);
        trainability = findViewById(R.id.trainability);
        demeanor = findViewById(R.id.demeanor);
        description = findViewById(R.id.description);

        // Get values from Intent
        Intent intent = getIntent();
        Glide.with(this).load(intent.getStringExtra("image_url")).into(dogImage);
        dogName.setText(intent.getStringExtra("dog_name"));
        bredFor.setText(intent.getStringExtra("bred_for"));
        breedGroup.setText(intent.getStringExtra("breed_group"));
        lifespan.setText(intent.getStringExtra("lifespan"));
        temperament.setText(intent.getStringExtra("temperament"));
        weightRange.setText(intent.getStringExtra("weight_range"));
        heightRange.setText(intent.getStringExtra("height_range"));
        expectancyRange.setText(intent.getStringExtra("expectancy_range"));
        grooming.setText(intent.getStringExtra("grooming"));
        shedding.setText(intent.getStringExtra("shedding"));
        energyLevel.setText(intent.getStringExtra("energy_level"));
        trainability.setText(intent.getStringExtra("trainability"));
        demeanor.setText(intent.getStringExtra("demeanor"));
        description.setText(intent.getStringExtra("description"));
    }

}
