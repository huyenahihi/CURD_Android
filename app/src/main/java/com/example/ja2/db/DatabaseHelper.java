package com.example.ja2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ja2.db.entity.Parking;
import com.example.ja2.db.entity.Task;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "hiker_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Parking.CREATE_TABLE);
        sqLiteDatabase.execSQL(Task.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Parking.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Task.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public Parking getParking(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Parking.TABLE_NAME, new String[]{Parking.COLUMN_ID, Parking.COLUMN_NAME, Parking.COLUMN_EMAIL, Parking.COLUMN_DESCRIPTION, Parking.COLUMN_LOCATION, Parking.COLUMN_LENGTH, Parking.COLUMN_LEVEL}, Parking.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();
        Parking parking = new Parking();
        parking.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Parking.COLUMN_ID)));
        parking.setName(cursor.getString(cursor.getColumnIndexOrThrow(Parking.COLUMN_NAME)));
        parking.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(Parking.COLUMN_EMAIL)));
        parking.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(Parking.COLUMN_DESCRIPTION)));
        parking.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(Parking.COLUMN_LOCATION)));
        parking.setLength(cursor.getDouble(cursor.getColumnIndexOrThrow(Parking.COLUMN_LENGTH)));
        parking.setLevel(cursor.getInt(cursor.getColumnIndexOrThrow(Parking.COLUMN_LEVEL)));
        cursor.close();
        return parking;
    }

    public ArrayList<Parking> getListParking() {
        ArrayList<Parking> mData = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Parking.TABLE_NAME + " ORDER BY " + Parking.COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Parking parking = new Parking();
                parking.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Parking.COLUMN_ID)));
                parking.setName(cursor.getString(cursor.getColumnIndexOrThrow(Parking.COLUMN_NAME)));
                parking.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(Parking.COLUMN_EMAIL)));
                parking.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(Parking.COLUMN_DESCRIPTION)));
                parking.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(Parking.COLUMN_LOCATION)));
                parking.setLength(cursor.getDouble(cursor.getColumnIndexOrThrow(Parking.COLUMN_LENGTH)));
                parking.setLevel(cursor.getInt(cursor.getColumnIndexOrThrow(Parking.COLUMN_LEVEL)));
                mData.add(parking);
            } while (cursor.moveToNext());
        }
        db.close();
        return mData;
    }

    // Insert Data into Database
    public long insertParking(Parking parking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Parking.COLUMN_NAME, parking.getName());
        values.put(Parking.COLUMN_EMAIL, parking.getEmail());
        values.put(Parking.COLUMN_DESCRIPTION, parking.getDescription());
        values.put(Parking.COLUMN_LOCATION, parking.getLocation());
        values.put(Parking.COLUMN_LENGTH, parking.getLength());
        values.put(Parking.COLUMN_LEVEL, parking.getLevel());
        long id = db.insert(Parking.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public int updateParking(Parking parking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Parking.COLUMN_NAME, parking.getName());
        values.put(Parking.COLUMN_EMAIL, parking.getEmail());
        values.put(Parking.COLUMN_DESCRIPTION, parking.getDescription());
        values.put(Parking.COLUMN_LOCATION, parking.getLocation());
        values.put(Parking.COLUMN_LENGTH, parking.getLength());
        values.put(Parking.COLUMN_LEVEL, parking.getLevel());
        int rowsUpdated = db.update(Parking.TABLE_NAME, values, Parking.COLUMN_ID + " = ? ", new String[]{String.valueOf(parking.getId())});
        db.close(); // Close the database
        return rowsUpdated;
    }

    public void deleteParking(Parking parking) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Parking.TABLE_NAME, Parking.COLUMN_ID + " = ?", new String[]{String.valueOf(parking.getId())});
        db.close();
    }

    public ArrayList<Parking> searchParking(String keyword) {
        ArrayList<Parking> parkings = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Parking.TABLE_NAME + " WHERE " + Parking.COLUMN_NAME + " LIKE '%" + keyword + "%' OR " + Parking.COLUMN_EMAIL + " LIKE '%" + keyword + "%' " +
                "OR " +  Parking.COLUMN_LOCATION + " LIKE '%" + keyword + "%' "+
                "OR " +  Parking.COLUMN_LENGTH + " LIKE '%" + keyword + "%' "+
        " ORDER BY " + Parking.COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Parking parking = new Parking();
                parking.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Parking.COLUMN_ID)));
                parking.setName(cursor.getString(cursor.getColumnIndexOrThrow(Parking.COLUMN_NAME)));
                parking.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(Parking.COLUMN_EMAIL)));
                parkings.add(parking);
            } while (cursor.moveToNext());
        }
        db.close();
        return parkings;
    }

    public ArrayList<Task> getListTask(long uid) {
        ArrayList<Task> mData = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Task.TABLE_NAME + " WHERE " + Task.COLUMN_UID + " = " + uid + " ORDER BY " + Task.COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_ID)));
                task.setNote(cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_NOTE)));
                task.setDateTime(cursor.getLong(cursor.getColumnIndexOrThrow(Task.COLUMN_DATE_TIME)));
                task.setUID(cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_UID)));
                mData.add(task);
            } while (cursor.moveToNext());
        }
        db.close();
        return mData;
    }

    public long insertTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Task.COLUMN_NOTE, task.getNote());
        values.put(Task.COLUMN_DATE_TIME, task.getDateTime());
        values.put(Task.COLUMN_UID, task.getUID());
        long id = db.insert(Task.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Task.COLUMN_ID, task.getId());
        values.put(Task.COLUMN_NOTE, task.getNote());
        values.put(Task.COLUMN_DATE_TIME, task.getDateTime());
        values.put(Task.COLUMN_UID, task.getUID());
        int rowsUpdated = db.update(Task.TABLE_NAME, values, Task.COLUMN_ID + " = ? ", new String[]{String.valueOf(task.getId())});
        db.close();
        return rowsUpdated;
    }
}
