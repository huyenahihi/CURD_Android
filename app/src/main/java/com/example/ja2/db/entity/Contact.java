package com.example.ja2.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Contact implements Parcelable {

    // 1- Constants for Database
    public static final String DATA_CONTACT = "DATA_CONTACT";
    public static final String TABLE_NAME = "contacts";
    public static final String COLUMN_ID = "contact_id";
    public static final String COLUMN_NAME = "contact_name";
    public static final String COLUMN_EMAIL = "contact_email";

    // 2- Variables
    private String name;
    private String email;
    private int id;

    // 3- Constructors
    public Contact(){

    }

    public Contact(String name, String email, int id){
        this.name = name;
        this.email = email;
        this.id = id;
    }

    protected Contact(Parcel in) {
        name = in.readString();
        email = in.readString();
        id = in.readInt();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    // 4- Getters & Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return "id: " + id + "\nName: " + name +"\nEmail: " + email;
    }

    // 5- SQL Query: Creating the Table
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
//                    + COLUMN_EMAIL + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + COLUMN_EMAIL + " TEXT"
                    + ")";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeInt(id);
    }
}
