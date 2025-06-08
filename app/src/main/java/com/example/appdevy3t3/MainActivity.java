package com.example.appdevy3t3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.Manifest;
import android.util.Log;


public class MainActivity extends AppCompatActivity {
    ImageButton btnpicture;
    static final int REQUEST_CODE = 22;
    private Uri photoURI;
    private String currentPhotoPath;

    // for database connection
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    ArrayList<DogModel> dogList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Makes sure the app doesn't overlap with the phone's UI (status bar, navigation bar, etc.)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Asks the phone for camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    100);
        }

        btnpicture = findViewById(R.id.cameraButton);

        btnpicture.setOnClickListener(new View.OnClickListener() {
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
                    photoURI = FileProvider.getUriForFile(MainActivity.this,
                            getPackageName() + ".fileprovider",
                            photoFile);

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_CODE);
                }
            }
        });

        RecyclerView rv_dogList = findViewById(R.id.dogLibrary);
        setUpDogModels();
        DogAdapter adapter = new DogAdapter(this, dogList);
        rv_dogList.setAdapter(adapter);
        rv_dogList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Intent intent = new Intent(MainActivity.this, BreedResults.class);
            intent.putExtra("image_path", photoURI.toString());
            startActivity(intent);
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

    private void setUpDogModels() {
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.openDatabase();

        String query = "SELECT\n" +
                "    b.id,\n" +
                "    b.name,\n" +
                "    b.bred_for,\n" +
                "    b.breed_group,\n" +
                "    b.life_span,\n" +
                "    b.temperament AS breed_temperament,\n" +
                "    b.reference_image_id,\n" +
                "    b.imperial_weight,\n" +
                "    b.metric_weight,\n" +
                "    b.imperial_height,\n" +
                "    b.metric_height,\n" +
                "    b.image_id,\n" +
                "    b.image_url,\n" +
                "    b.image_width,\n" +
                "    b.image_height,\n" +
                "    b.image_blob,\n" +
                "    \n" +
                "    akc.description,\n" +
                "    akc.temperament AS akc_temperament,\n" +
                "    ROUND(akc.min_height, 2) AS min_height,\n" +
                "    ROUND(akc.max_height, 2) AS max_height,\n" +
                "    ROUND(akc.min_weight, 2) AS min_weight,\n" +
                "    ROUND(akc.max_weight, 2) AS max_weight,\n" +
                "    ROUND(akc.min_expectancy, 2) AS min_expectancy,\n" +
                "    ROUND(akc.max_expectancy, 2) AS max_expectancy,\n" +
                "    akc.grooming_frequency_category,\n" +
                "    akc.shedding_category,\n" +
                "    akc.energy_level_category,\n" +
                "    akc.trainability_category,\n" +
                "    akc.demeanor_category\n" +
                "FROM breeds AS b\n" +
                "JOIN akc_data AS akc ON akc.name = b.name\n" +
                "WHERE\n" +
                "    b.bred_for IS NOT NULL\n" +
                "    AND b.breed_group IS NOT NULL\n" +
                "    AND b.life_span IS NOT NULL\n" +
                "    AND b.temperament IS NOT NULL\n" +
                "    AND akc.min_height IS NOT NULL\n" +
                "    AND akc.max_height IS NOT NULL\n" +
                "    AND akc.min_weight IS NOT NULL\n" +
                "    AND akc.max_weight IS NOT NULL\n" +
                "    AND akc.min_expectancy IS NOT NULL\n" +
                "    AND akc.max_expectancy IS NOT NULL\n" +
                "    AND akc.grooming_frequency_category IS NOT NULL\n" +
                "    AND akc.shedding_category IS NOT NULL\n" +
                "    AND akc.energy_level_category IS NOT NULL\n" +
                "    AND akc.trainability_category IS NOT NULL\n" +
                "    AND akc.demeanor_category IS NOT NULL\n" +
                "    AND akc.description IS NOT NULL;\n";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String dog_name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("image_url"));

                String bredFor = cursor.getString(cursor.getColumnIndexOrThrow("bred_for"));
                String breedGroup = cursor.getString(cursor.getColumnIndexOrThrow("breed_group"));
                String lifespan = cursor.getString(cursor.getColumnIndexOrThrow("life_span"));
                String temperament = cursor.getString(cursor.getColumnIndexOrThrow("breed_temperament"));
                String weightRange = cursor.getString(cursor.getColumnIndexOrThrow("min_weight")) + " kg - " +
                        cursor.getString(cursor.getColumnIndexOrThrow("max_weight")) + " kg";
                String heightRange = cursor.getString(cursor.getColumnIndexOrThrow("min_height")) + " cm - " +
                        cursor.getString(cursor.getColumnIndexOrThrow("max_height")) + " cm";
                String expectancyRange = cursor.getString(cursor.getColumnIndexOrThrow("min_expectancy")) + " years - " +
                        cursor.getString(cursor.getColumnIndexOrThrow("max_expectancy")) + " years";
                String grooming = cursor.getString(cursor.getColumnIndexOrThrow("grooming_frequency_category"));
                String shedding = cursor.getString(cursor.getColumnIndexOrThrow("shedding_category"));
                String energyLevel = cursor.getString(cursor.getColumnIndexOrThrow("energy_level_category"));
                String trainability = cursor.getString(cursor.getColumnIndexOrThrow("trainability_category"));
                String demeanor = cursor.getString(cursor.getColumnIndexOrThrow("demeanor_category"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

                dogList.add(new DogModel(dog_name, imageUrl,
                        bredFor, breedGroup, lifespan, temperament,
                        weightRange, heightRange, expectancyRange, grooming,
                         shedding, energyLevel, trainability, demeanor,
                         description));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

//    private void setUpDogModels() {
//        dbHelper = new DatabaseHelper(this);
//        db = dbHelper.openDatabase();
//
//        Cursor cursor = db.rawQuery("SELECT name, image_url, bred_for, breed_group, life_span, temperament, " +
//                "min_weight, max_weight, min_height, max_height, min_expectancy, max_expectancy, grooming, " +
//                "shedding, energy_level, trainability, demeanor FROM breeds", null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
//                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("image_url"));
//                String bredFor = cursor.getString(cursor.getColumnIndexOrThrow("bred_for"));
//                String breedGroup = cursor.getString(cursor.getColumnIndexOrThrow("breed_group"));
//                String lifeSpan = cursor.getString(cursor.getColumnIndexOrThrow("life_span"));
//                String temperament = cursor.getString(cursor.getColumnIndexOrThrow("temperament"));
//                String weightMin = cursor.getString(cursor.getColumnIndexOrThrow("min_weight"));
//                String weightMax = cursor.getString(cursor.getColumnIndexOrThrow("max_weight"));
//                String heightMin = cursor.getString(cursor.getColumnIndexOrThrow("min_height"));
//                String heightMax = cursor.getString(cursor.getColumnIndexOrThrow("max_height"));
//                String expectancyMin = cursor.getString(cursor.getColumnIndexOrThrow("min_expectancy"));
//                String expectancyMax = cursor.getString(cursor.getColumnIndexOrThrow("max_expectancy"));
//                String grooming = cursor.getString(cursor.getColumnIndexOrThrow("grooming"));
//                String shedding = cursor.getString(cursor.getColumnIndexOrThrow("shedding"));
//                String energyLevel = cursor.getString(cursor.getColumnIndexOrThrow("energy_level"));
//                String trainability = cursor.getString(cursor.getColumnIndexOrThrow("trainability"));
//                String demeanor = cursor.getString(cursor.getColumnIndexOrThrow("demeanor"));
//
//                DogModel dog = new DogModel(
//                        name, imageUrl, bredFor, breedGroup, lifeSpan, temperament,
//                        weightMin, weightMax, heightMin, heightMax,
//                        expectancyMin, expectancyMax, grooming, shedding,
//                        energyLevel, trainability, demeanor
//                );
//                dogList.add(dog);
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//        db.close();
//    }
}

