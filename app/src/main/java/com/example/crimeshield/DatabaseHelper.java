package com.example.crimeshield;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context c) {
        super(c, "CrimeShield.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE reports (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "crime_type TEXT, " +
                "location TEXT, " +
                "severity TEXT, " +
                "description TEXT, " +
                "date TEXT, " +
                "status TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int o, int n) {
        db.execSQL("DROP TABLE IF EXISTS reports");
        onCreate(db);
    }

    public boolean insertReport(String c, String l, String s, String d, String dt) {
        ContentValues v = new ContentValues();
        v.put("crime_type", c);
        v.put("location", l);
        v.put("severity", s);
        v.put("description", d);
        v.put("date", dt);
        v.put("status", "Pending");
        
        long result = getWritableDatabase().insert("reports", null, v);
        return result != -1;
    }
}