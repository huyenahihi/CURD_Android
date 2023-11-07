package com.example.ja2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ja2.db.entity.Contact;
import com.example.ja2.db.entity.Task;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contact_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Contact.CREATE_TABLE);
        sqLiteDatabase.execSQL(Task.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contact.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Task.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    // Getting Contact from DataBase
    public Contact getContact(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Contact.TABLE_NAME, new String[]{Contact.COLUMN_ID, Contact.COLUMN_NAME, Contact.COLUMN_EMAIL}, Contact.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();
        Contact contact = new Contact(cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_NAME)), cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_EMAIL)), cursor.getInt(cursor.getColumnIndexOrThrow(Contact.COLUMN_ID)));
        cursor.close();
        return contact;
    }

    // Getting all Contacts
    public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> contacts = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Contact.TABLE_NAME + " ORDER BY " + Contact.COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Contact.COLUMN_ID)));
                contact.setName(cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_NAME)));
                contact.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_EMAIL)));
                contacts.add(contact);
            } while (cursor.moveToNext());
        }
        db.close();
        return contacts;
    }

    // Insert Data into Database
    public long insertContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contact.COLUMN_NAME, contact.getName());
        values.put(Contact.COLUMN_EMAIL, contact.getEmail());
        long id = db.insert(Contact.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contact.COLUMN_NAME, contact.getName());
        values.put(Contact.COLUMN_EMAIL, contact.getEmail());
//        return db.update(Contact.TABLE_NAME, values,Contact.COLUMN_ID+ " = ? ",
//                new String[]{String.valueOf(contact.getId())});
        int rowsUpdated = db.update(Contact.TABLE_NAME, values, Contact.COLUMN_ID + " = ? ", new String[]{String.valueOf(contact.getId())});
        db.close(); // Close the database
        return rowsUpdated;
    }

    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Contact.TABLE_NAME, Contact.COLUMN_ID + " = ?", new String[]{String.valueOf(contact.getId())});
        db.close();
    }

    public ArrayList<Contact> searchContacts(String keyword) {
        ArrayList<Contact> contacts = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Contact.TABLE_NAME + " WHERE " + Contact.COLUMN_NAME + " LIKE '%" + keyword + "%' OR " + Contact.COLUMN_EMAIL + " LIKE '%" + keyword + "%' ORDER BY " + Contact.COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Contact.COLUMN_ID)));
                contact.setName(cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_NAME)));
                contact.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_EMAIL)));
                contacts.add(contact);
            } while (cursor.moveToNext());
        }
        db.close();
        return contacts;
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
