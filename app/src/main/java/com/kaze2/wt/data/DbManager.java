package com.kaze2.wt.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DbManager {
  private final DatabaseHelper dbHelper;

  private SQLiteDatabase database;

  public DbManager(Context context) {
    dbHelper = new DatabaseHelper(context);
  }

  public DbManager open() throws SQLException {
    database = dbHelper.getWritableDatabase();
    return this;
  }

  public void close() {
    dbHelper.close();
  }

  public void insert(String continent, String city, String timeOffset) {
    final ContentValues contentValue = new ContentValues();

    contentValue.put(DatabaseHelper.CONTINENT, continent);
    contentValue.put(DatabaseHelper.CITY, city);
    contentValue.put(DatabaseHelper.TIME_OFFSET, timeOffset);

    database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
  }

  public Cursor fetch() {
    final String[] columns =
        new String[] {
          DatabaseHelper._ID,
          DatabaseHelper.CONTINENT,
          DatabaseHelper.CITY,
          DatabaseHelper.TIME_OFFSET
        };

    final Cursor cursor =
        database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);

    if (cursor != null) {
      cursor.moveToFirst();
    }

    return cursor;
  }

  public int update(long _id, String continent, String city, String timeOffset) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(DatabaseHelper.CONTINENT, continent);
    contentValues.put(DatabaseHelper.CITY, city);
    contentValues.put(DatabaseHelper.TIME_OFFSET, timeOffset);
    return database.update(
        DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
  }

  public void delete(long _id) {
    database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
  }
}
