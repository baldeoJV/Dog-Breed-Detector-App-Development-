package com.example.appdevy3t3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BreedResults extends AppCompatActivity {
    ImageButton backButton;
    ImageButton cameraButton;
    static final int REQUEST_CODE = 22;
    private Uri photoURI;
    private String currentPhotoPath;
    TextView dogName, bredFor, breedGroup, lifespan, temperament,
            weightRange, heightRange, expectancyRange,
            grooming, shedding, energyLevel, trainability, demeanor, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_breed_results);

        //Makes sure the app doesn't overlap with the phone's UI (status bar, navigation bar, etc.)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Dog_Information), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView imageResults = findViewById(R.id.dogImage);
        String imagePath = getIntent().getStringExtra("image_path");

        // Initialize your views
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

        if (imagePath != null) {
            Uri imageUri = Uri.parse(imagePath);
            imageResults.setImageURI(imageUri);
        }

        backButton = findViewById(R.id.backButton);
        cameraButton = findViewById(R.id.button2);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BreedResults.this, MainActivity.class);
                startActivity(intent);
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                if (photoFile != null) {
                    photoURI = FileProvider.getUriForFile(BreedResults.this,
                            getPackageName() + ".fileprovider",
                            photoFile);

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_CODE);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (photoURI != null) {
                ImageView imageResults = findViewById(R.id.dogImage);
                imageResults.setImageURI(photoURI);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentPhotoPath = image.getAbsolutePath(); // Store the file path for later use
        return image;
    }
}