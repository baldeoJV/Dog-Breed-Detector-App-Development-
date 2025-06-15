package com.example.appdevy3t3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import android.graphics.Bitmap;

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
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.util.Log;

import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.Interpreter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
public class MainActivity extends AppCompatActivity {
    int NUM_CLASSES = 120;
    ImageButton btnpicture;
    static final int REQUEST_CODE = 22;
    private Uri photoURI;
    private String currentPhotoPath;

    // for database connection
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    ArrayList<DogModel> dogList = new ArrayList<>();
    int imageSize = 224;
    List<String> classNames;
    String predictedDogName = "";



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

        //load the dog_breeds.txt for the model
        try {
            classNames = FileUtil.loadLabels(this, "dog_breeds.txt");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load labels", Toast.LENGTH_SHORT).show();
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
                // Normalize RGB values to [0,1] by dividing by 255
                byteBuffer.putFloat(((val >> 16) & 0xFF));
                byteBuffer.putFloat(((val >> 8) & 0xFF));
                byteBuffer.putFloat((val & 0xFF));
            }
        }
        return byteBuffer;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
                new AlertDialog.Builder(this)
                        .setTitle("Reminder")
                        .setMessage("The breed prediction is not 100% accurate and is based solely on the dog's appearance. For a more reliable assessment, consult a professional.")
                        .setPositiveButton("OK", (dialog, which) -> {
                            // Call your method only after user taps OK
                            try {
                                setUpDgoModels2();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .setCancelable(false) // Optional: prevent dismiss by tapping outside
                        .show();
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

    private void setUpDgoModels2() throws IOException {
        // Convert URI to bitmap and resize the image to 32x32 pixels
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoURI);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, true);

        //classify the dog breed
        classifyImage(resizedBitmap);

        Intent intent = new Intent(MainActivity.this, BreedResults.class);

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
                "    AND akc.description IS NOT NULL\n" +
                "    AND b.name LIKE '%" + predictedDogName + "%' COLLATE NOCASE;";


        Cursor cursor = db.rawQuery(query, null);
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

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        intent.putExtra("image_path", photoURI.toString()); // pass the image
        intent.putExtra("predicted_dog_name", predictedDogName); // pass the dog name
        startActivity(intent);
    }


}

