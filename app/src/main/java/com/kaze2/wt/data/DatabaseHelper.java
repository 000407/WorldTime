package com.kaze2.wt.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "LOCATIONS";

    // Table columns
    public static final String _ID = "_id";
    public static final String CONTINENT = "continent";
    public static final String CITY = "city";
    public static final String TIME_OFFSET = "time_offset";

    // Database Information
    static final String DB_NAME = "DB_WORLD_TIME";

    // database version
    static final int DB_VERSION = 1;

  // Creating table query
  private static final String CREATE_TABLE =
      "CREATE TABLE IF NOT EXISTS "
          + TABLE_NAME
          + "("
          + _ID
          + " INTEGER PRIMARY KEY AUTOINCREMENT, "
          + CONTINENT
          + " TEXT NOT NULL, "
          + CITY
          + " TEXT NOT NULL, "
          + TIME_OFFSET
          + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
