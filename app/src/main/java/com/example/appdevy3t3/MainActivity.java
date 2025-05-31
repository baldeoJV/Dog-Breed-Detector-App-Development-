package com.example.appdevy3t3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
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
    RecyclerView rv_dogList;

    // for database connection
    DatabaseHelper dbHelper;
    SQLiteDatabase db;

    ArrayList<DogModel> dogModels = new ArrayList<>();
    int[] dogImages = {R.drawable.labrador, R.drawable.german_shepard, R.drawable.golden_retriever,
            R.drawable.bulldog, R.drawable.beagle, R.drawable.poodle, R.drawable.rottweiler,
            R.drawable.dachshund, R.drawable.siberian_husky, R.drawable.shih_tzu};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

//
//        RecyclerView recyclerView = findViewById(R.id.dogLibrary);
//        setUpDogModels();
//        Dog_RecyclerViewAdapter adapter = new Dog_RecyclerViewAdapter(this, dogModels);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView rv_dogList = findViewById(R.id.dogLibrary);
        rv_dogList.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<DogModel> dogList = new ArrayList<>();
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.openDatabase();

        Cursor cursor = db.rawQuery("SELECT name, image_url FROM breeds", null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("image_url"));
                dogList.add(new DogModel(name, imageUrl));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        DogAdapter adapter = new DogAdapter(this, dogList);
        rv_dogList.setAdapter(adapter);

//        Cursor cursor = null;
//        try {
//            cursor = db.rawQuery("SELECT name, image_url FROM breeds", null);
//
//            if (cursor.moveToFirst()) {
//                do {
//                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
//                    String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("image_url"));
//                    dogList.add(new DogModel(name, imageUrl));
//                } while (cursor.moveToNext());
//            }
//        } catch (Exception e) {
//            Log.e("DB_ERROR", "Query failed", e);
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            db.close();
//        }

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

//    private void setUpDogModels() {
//        String[] dogNames = getResources().getStringArray(R.array.dog_breeds_full_text);
//
//        for (int i = 0; i < dogNames.length; i++) {
//            dogModels.add(new DogModel(dogNames[i], dogImages[i]));
//        }
//    }
}

