package com.example.ja2.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Task implements Parcelable {

    public static final String DATA_TASK = "DATA_TASK";
    public static final String TABLE_NAME = "tbl_task";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PARKING_ID = "parking_id";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_DATE_TIME = "date_time";

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NOTE + " TEXT,"
                    + COLUMN_DATE_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + COLUMN_PARKING_ID + " INTEGER, "
                    + "FOREIGN KEY(" + COLUMN_PARKING_ID + ") REFERENCES " + Parking.TABLE_NAME + "("+Parking.COLUMN_ID+")"
                    + ")";
    private long dateTime;
    private String note;
    private int uid;
    private int id;

    public Task() {

    }

    public Task(long dateTime, String note, int uid) {
        this.dateTime = dateTime;
        this.note = note;
        this.uid = uid;
    }

    public Task(long dateTime, String note, int uid, int id) {
        this.dateTime = dateTime;
        this.note = note;
        this.uid = uid;
        this.id = id;
    }

    protected Task(Parcel in) {
        dateTime = in.readLong();
        note = in.readString();
        uid = in.readInt();
        id = in.readInt();
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getUID() {
        return uid;
    }

    public void setUID(int uid) {
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Task{" + "dateTime = '" + dateTime + '\'' + ",note = '" + note + "',uid = '" + uid + '\'' + ",id = '" + id + '\'' + "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(dateTime);
        dest.writeString(note);
        dest.writeInt(uid);
        dest.writeInt(id);
    }
}