package com.example.ja2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ja2.db.entity.Hike;
import com.example.ja2.db.entity.Observation;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "hiker.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    //create table
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Hike.CREATE_TABLE);
        sqLiteDatabase.execSQL(Observation.CREATE_TABLE);
    }

    @Override
    //Check nếu có bảng rồi thì xóa bảng
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Hike.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Observation.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public Hike getHike(long id) { //getParking
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Hike.TABLE_NAME, new String[]{Hike.COLUMN_ID, Hike.COLUMN_NAME,  Hike.COLUMN_DESCRIPTION, Hike.COLUMN_LOCATION, Hike.COLUMN_LENGTH, Hike.COLUMN_AVAILABLE, Hike.COLUMN_LEVEL, Hike.COLUMN_DATE}, Hike.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();
        Hike hike = new Hike();
        hike.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Hike.COLUMN_ID)));
        hike.setName(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_NAME)));
        hike.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_DESCRIPTION)));
        hike.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_LOCATION)));
        hike.setLength(cursor.getDouble(cursor.getColumnIndexOrThrow(Hike.COLUMN_LENGTH)));
        int status = cursor.getInt(cursor.getColumnIndexOrThrow(Hike.COLUMN_AVAILABLE));
        hike.setAvailable(status != 0);
        hike.setLevel(cursor.getInt(cursor.getColumnIndexOrThrow(Hike.COLUMN_LEVEL)));
        hike.setDate(cursor.getLong(cursor.getColumnIndexOrThrow(Hike.COLUMN_DATE)));
        cursor.close();
        return hike;
    }

    public ArrayList<Hike> getListHike() {
        ArrayList<Hike> mData = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Hike.TABLE_NAME + " ORDER BY " + Hike.COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Hike hike = new Hike();
                hike.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Hike.COLUMN_ID)));
                hike.setName(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_NAME)));
                hike.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_DESCRIPTION)));
                hike.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_LOCATION)));
                hike.setLength(cursor.getDouble(cursor.getColumnIndexOrThrow(Hike.COLUMN_LENGTH)));
                int status = cursor.getInt(cursor.getColumnIndexOrThrow(Hike.COLUMN_AVAILABLE));
                hike.setAvailable(status != 0);
                hike.setLevel(cursor.getInt(cursor.getColumnIndexOrThrow(Hike.COLUMN_LEVEL)));
                hike.setDate(cursor.getLong(cursor.getColumnIndexOrThrow(Hike.COLUMN_DATE)));
                mData.add(hike);
            } while (cursor.moveToNext());
        }
        db.close();
        return mData;
    }

    // Insert Data into Database
    public long insertHike(Hike hike) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Hike.COLUMN_NAME, hike.getName());
        values.put(Hike.COLUMN_DESCRIPTION, hike.getDescription());
        values.put(Hike.COLUMN_LOCATION, hike.getLocation());
        values.put(Hike.COLUMN_LENGTH, hike.getLength());
        values.put(Hike.COLUMN_AVAILABLE, hike.getAvailable());
        values.put(Hike.COLUMN_LEVEL, hike.getLevel());
        values.put(Hike.COLUMN_DATE, hike.getDate());
        long id = db.insert(Hike.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public int updateHike(Hike hike) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Hike.COLUMN_NAME, hike.getName());
        values.put(Hike.COLUMN_DESCRIPTION, hike.getDescription());
        values.put(Hike.COLUMN_LOCATION, hike.getLocation());
        values.put(Hike.COLUMN_LENGTH, hike.getLength());
        values.put(Hike.COLUMN_AVAILABLE, hike.getAvailable());
        values.put(Hike.COLUMN_LEVEL, hike.getLevel());
        values.put(Hike.COLUMN_DATE, hike.getDate());
        int rowsUpdated = db.update(Hike.TABLE_NAME, values, Hike.COLUMN_ID + " = ? ", new String[]{String.valueOf(hike.getId())});
        db.close();
        return rowsUpdated;
    }

    public void deleteHike(Hike hike) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Hike.TABLE_NAME, Hike.COLUMN_ID + " = ?", new String[]{String.valueOf(hike.getId())});
        db.close();
    }

    public ArrayList<Hike> searchHike(String keyword) {
        ArrayList<Hike> hikes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Hike.TABLE_NAME + " WHERE " + Hike.COLUMN_NAME + " LIKE '%" + keyword + "%' OR " + Hike.COLUMN_LOCATION + " LIKE '%" + keyword + "%' " + "OR " + Hike.COLUMN_LENGTH + " LIKE '%" + keyword + "%' " + " ORDER BY " + Hike.COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Hike hike = new Hike();
                hike.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Hike.COLUMN_ID)));
                hike.setName(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COLUMN_NAME)));
                hikes.add(hike);
            } while (cursor.moveToNext());
        }
        db.close();
        return hikes;
    }

    public ArrayList<Observation> getListObservation(long uid) {
        ArrayList<Observation> mData = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Observation.TABLE_NAME + " WHERE " + Observation.COLUMN_HIKE_ID + " = " + uid + " ORDER BY " + Observation.COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Observation observation = new Observation();
                observation.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Observation.COLUMN_ID)));
                observation.setNote(cursor.getString(cursor.getColumnIndexOrThrow(Observation.COLUMN_NOTE)));
                observation.setDateTime(cursor.getLong(cursor.getColumnIndexOrThrow(Observation.COLUMN_DATE_TIME)));
                observation.setUID(cursor.getInt(cursor.getColumnIndexOrThrow(Observation.COLUMN_HIKE_ID)));
                mData.add(observation);
            } while (cursor.moveToNext());
        }
        db.close();
        return mData;
    }

    public long insertObservation(Observation observation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Observation.COLUMN_NOTE, observation.getNote());
        values.put(Observation.COLUMN_DATE_TIME, observation.getDateTime());
        values.put(Observation.COLUMN_HIKE_ID, observation.getUID());
        long id = db.insert(Observation.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public int updateObservation(Observation observation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Observation.COLUMN_ID, observation.getId());
        values.put(Observation.COLUMN_NOTE, observation.getNote());
        values.put(Observation.COLUMN_DATE_TIME, observation.getDateTime());
        values.put(Observation.COLUMN_HIKE_ID, observation.getUID());
        int rowsUpdated = db.update(Observation.TABLE_NAME, values, Observation.COLUMN_ID + " = ? ", new String[]{String.valueOf(observation.getId())});
        db.close();
        return rowsUpdated;
    }
}
