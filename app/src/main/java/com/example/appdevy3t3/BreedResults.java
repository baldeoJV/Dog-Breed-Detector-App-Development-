package com.example.appdevy3t3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
    // for database connection
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    int imageSize = 224;
    List<String> classNames;
    String predictedDogName = "";
    int NUM_CLASSES = 120;

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

        //load the dog_breeds.txt for the model
        try {
            classNames = FileUtil.loadLabels(this, "dog_breeds.txt");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load labels", Toast.LENGTH_SHORT).show();
        }

        // alert after predicting the dog model
        new AlertDialog.Builder(this)
                .setTitle("Reminder")
                .setMessage("The breed prediction is not 100% accurate and is based solely on the dog's appearance. For a more reliable assessment, consult a professional.")
                .setPositiveButton("OK", (dialog, which) -> {})
                .setCancelable(false) // Optional: prevent dismiss by tapping outside
                .show();

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
        backButton = findViewById(R.id.backButton);

        try {
            Intent intent = getIntent();

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

            String predicted_dog_name = intent.getStringExtra("predicted_dog_name");

            // Check if data is missing
            if (name == null) {
                dogName.setText(predicted_dog_name);
                bredFor.setText("(No Information)");
                breedGroup.setText("(No Information)");
                lifespan.setText("(No Information)");
                temperament.setText("(No Information)");
                weightRange.setText("(No Information)");
                heightRange.setText("(No Information)");
                expectancyRange.setText("(No Information)");
                grooming.setText("(No Information)");
                shedding.setText("(No Information)");
                energyLevel.setText("(No Information)");
                trainability.setText("(No Information)");
                demeanor.setText("(No Information)");
                description.setText("(No Information)");
            } else {
                dogName.setText(predicted_dog_name);
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
            }

        } catch (Exception e) {
            // Fallback if something goes wrong
            dogName.setText("No information for this breed");
            bredFor.setText("-");
            breedGroup.setText("-");
            lifespan.setText("-");
            temperament.setText("-");
            weightRange.setText("-");
            heightRange.setText("-");
            expectancyRange.setText("-");
            grooming.setText("-");
            shedding.setText("-");
            energyLevel.setText("-");
            trainability.setText("-");
            demeanor.setText("-");
            description.setText("-");
        }

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
        try {
            if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
                try {
                    setUpDgoModels2();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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

    public void classifyImage(Bitmap image) {
        try {
            // Load the model
            Interpreter interpreter = new Interpreter(
                    FileUtil.loadMappedFile(this, "model_experimental_v2_224.tflite"));

            // Preprocess image (resize + normalize)
            Bitmap resized = Bitmap.createScaledBitmap(image, 224, 224, true);
            ByteBuffer inputBuffer = convertBitmapToByteBuffer(resized);

            // Create output buffer (adjust size according to your model's output)
            float[][] output = new float[1][NUM_CLASSES];  // based on the model size output

            // Run inference
            interpreter.run(inputBuffer, output);

            // Find the highest confidence label
            int maxIndex = 0;
            float maxProb = 0;
            for (int i = 0; i < output[0].length; i++) {
                if (output[0][i] > maxProb) {
                    maxProb = output[0][i];
                    maxIndex = i;
                }
            }

            predictedDogName = classNames.get(maxIndex); // store it in the class variable
            Log.d("TFLITE", "Predicted: " + predictedDogName + " (confidence: " + maxProb + ")");
            interpreter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3);
        byteBuffer.order(ByteOrder.nativeOrder());

        int[] intValues = new int[224 * 224];
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        int pixel = 0;
        for (int i = 0; i < 224; ++i) {
            for (int j = 0; j < 224; ++j) {
                int val = intValues[pixel++];

                byteBuffer.putFloat(((val >> 16) & 0xFF));
                byteBuffer.putFloat(((val >> 8) & 0xFF));
                byteBuffer.putFloat((val & 0xFF));
            }
        }
        return byteBuffer;
    }


    private void setUpDgoModels2() throws IOException {
        // Convert URI to bitmap and resize the image to 32x32 pixels
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoURI);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, true);

        //classify the dog breed
        classifyImage(resizedBitmap);

        Intent intent = new Intent(BreedResults.this, BreedResults.class);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.openDatabase();

        String query = "SELECT\n" +
                "    akc.name,\n" +
                "    b.bred_for,\n" +
                "    b.breed_group,\n" +
                "    b.life_span,\n" +
                "    b.temperament AS breed_temperament,\n" +
                "    b.image_url,\n" +
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
                "FROM akc_data AS akc\n" +
                "LEFT JOIN breeds AS b ON akc.name = b.name\n" +
                "WHERE\n" +
//                "    b.bred_for IS NOT NULL\n" +
//                "    AND b.breed_group IS NOT NULL\n" +
//                "    AND b.life_span IS NOT NULL\n" +
//                "    AND b.temperament IS NOT NULL\n" +
//                "    AND akc.min_height IS NOT NULL\n" +
//                "    AND akc.max_height IS NOT NULL\n" +
//                "    AND akc.min_weight IS NOT NULL\n" +
//                "    AND akc.max_weight IS NOT NULL\n" +
//                "    AND akc.min_expectancy IS NOT NULL\n" +
//                "    AND akc.max_expectancy IS NOT NULL\n" +
//                "    AND akc.grooming_frequency_category IS NOT NULL\n" +
//                "    AND akc.shedding_category IS NOT NULL\n" +
//                "    AND akc.energy_level_category IS NOT NULL\n" +
//                "    AND akc.trainability_category IS NOT NULL\n" +
//                "    AND akc.demeanor_category IS NOT NULL\n" +
//                "    AND akc.description IS NOT NULL\n" +
                  "    b.name LIKE '%" + predictedDogName + "%' COLLATE NOCASE;";

        Cursor cursor = db.rawQuery(query, null);

        // Log the number of rows returned
        Log.d("DogLibrary", "Number of rows returned: " + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                String dog_name = cursor.getString(cursor.getColumnIndexOrThrow("name"));

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

                intent.putExtra("dog_name", dog_name);

                intent.putExtra("bred_for", bredFor);
                intent.putExtra("breed_group", breedGroup);
                intent.putExtra("lifespan", lifespan);
                intent.putExtra("temperament", temperament);
                intent.putExtra("weight_range", weightRange);
                intent.putExtra("height_range", heightRange);
                intent.putExtra("expectancy_range", expectancyRange);
                intent.putExtra("grooming", grooming);
                intent.putExtra("shedding", shedding);
                intent.putExtra("energy_level", energyLevel);
                intent.putExtra("trainability", trainability);
                intent.putExtra("demeanor", demeanor);
                intent.putExtra("description", description);


                Log.d("DogNameQuery", "Dog name query: " + cursor.getString(cursor.getColumnIndexOrThrow("name")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        intent.putExtra("image_path", photoURI.toString()); // pass the image
        intent.putExtra("predicted_dog_name", predictedDogName); // pass the dog name
        startActivity(intent);
    }

}