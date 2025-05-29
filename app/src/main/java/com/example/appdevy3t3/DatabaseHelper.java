package com.example.appdevy3t3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "dog_breeds_with_image.db";
    private static String DB_PATH = "";
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        copyDatabase();
    }

    private void copyDatabase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        if (!dbFile.exists()) {
            this.getReadableDatabase(); // creates empty db to overwrite
            try {
                InputStream is = context.getAssets().open(DB_NAME);
                OutputStream os = new FileOutputStream(dbFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Not needed since there's a database already
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not needed unless updating DB version (probably useless...)
    }

    public SQLiteDatabase openDatabase() {
        return SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
    }
}

