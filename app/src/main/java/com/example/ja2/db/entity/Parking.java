package com.example.ja2.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Parking implements Parcelable {

    // 1- Constants for Database
    public static final String DATA_PARKING = "DATA_PARKING";
    public static final String TABLE_NAME = "tbl_parking";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_AVAILABLE = "available";
    public static final String COLUMN_LENGTH = "length";
    public static final String COLUMN_LEVEL = "level";
    public static final String COLUMN_DATE = "date";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_DESCRIPTION + " TEXT,"
            + COLUMN_LOCATION + " TEXT,"
            + COLUMN_AVAILABLE + " BOOLEAN,"
            + COLUMN_LENGTH + " DOUBLE,"
            + COLUMN_LEVEL + " INTEGER,"
            + COLUMN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
    // 2- Variables
    private int id;
    private String name;
    private String description;
    private String location;
    private boolean available;
    private Double length = 0.0;
    private int level = 1;
    private long date = System.currentTimeMillis();

    // 3- Constructors
    public Parking() {

    }

    public Parking(String name, String description, String location, boolean available, Double length, int level, long date) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.available = available;
        this.length = length;
        this.level = level;
        this.date = date;
    }

    protected Parking(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        location = in.readString();
        available = in.readByte() != 0;
        length = in.readDouble();
        level = in.readInt();
        date = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(location);
        dest.writeByte((byte) (available ? 1 : 0));
        dest.writeDouble(length);
        dest.writeInt(level);
        dest.writeLong(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Parking> CREATOR = new Creator<Parking>() {
        @Override
        public Parking createFromParcel(Parcel in) {
            return new Parking(in);
        }

        @Override
        public Parking[] newArray(int size) {
            return new Parking[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String toString(){
        return "Parking{ 'id' = " + id  +",  name = '" + name + '\'' + ",description = '" + description + '\'' + ",location = '" + location + '\'' + ", available: " + available +"}";
    }

}
