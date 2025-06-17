package com.example.appdevy3t3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class Dog_Library_Results extends AppCompatActivity {

    ImageView dogImage;
    ImageButton backButton;
    TextView dogName, bredFor, breedGroup, lifespan, temperament,
            weightRange, heightRange, expectancyRange,
            grooming, shedding, energyLevel, trainability, demeanor, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_library); // Make sure this layout exists
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Dog_Information), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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

        String name = intent.getStringExtra("dog_name");
        String bredForStr = intent.getStringExtra("bred_for");
        String breedGroupStr = intent.getStringExtra("breed_group");
        String lifespanStr = intent.getStringExtra("lifespan");
        String temperamentStr = intent.getStringExtra("temperament");
        String weightRangeStr = intent.getStringExtra("weight_range");
        String heightRangeStr = intent.getStringExtra("height_range");
        String expectancyRangeStr = intent.getStringExtra("expectancy_range");
        String groomingStr = intent.getStringExtra("grooming");
        String sheddingStr = intent.getStringExtra("shedding");
        String energyLevelStr = intent.getStringExtra("energy_level");
        String trainabilityStr = intent.getStringExtra("trainability");
        String demeanorStr = intent.getStringExtra("demeanor");
        String descriptionStr = intent.getStringExtra("description");

        dogName.setText(name);
        bredFor.setText(bredForStr != null ? bredForStr : "(No Information)");
        breedGroup.setText(breedGroupStr != null ? breedGroupStr : "(No Information)");
        lifespan.setText(lifespanStr != null ? lifespanStr : "(No Information)");
        temperament.setText(temperamentStr != null ? temperamentStr : "(No Information)");
        weightRange.setText(weightRangeStr != null ? weightRangeStr : "(No Information)");
        heightRange.setText(heightRangeStr != null ? heightRangeStr : "(No Information)");
        expectancyRange.setText(expectancyRangeStr != null ? expectancyRangeStr : "(No Information)");
        grooming.setText(groomingStr != null ? groomingStr : "(No Information)");
        shedding.setText(sheddingStr != null ? sheddingStr : "(No Information)");
        energyLevel.setText(energyLevelStr != null ? energyLevelStr : "(No Information)");
        trainability.setText(trainabilityStr != null ? trainabilityStr : "(No Information)");
        demeanor.setText(demeanorStr != null ? demeanorStr : "(No Information)");
        description.setText(descriptionStr != null ? descriptionStr : "(No Information)");

        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dog_Library_Results.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
