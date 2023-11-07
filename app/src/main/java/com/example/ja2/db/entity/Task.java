package com.example.ja2.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Task implements Parcelable {

    public static final String DATA_TASK = "DATA_TASK";
    public static final String TABLE_NAME = "tbl_task";
    public static final String COLUMN_ID = "id";
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
    private String dateTime;
    private String note;
    private int id;

    public Task() {

    }

    public Task(String dateTime, String note) {
        this.dateTime = dateTime;
        this.note = note;
    }

    public Task(String dateTime, String note, int id) {
        this.dateTime = dateTime;
        this.note = note;
        this.id = id;
    }

    protected Task(Parcel in) {
        dateTime = in.readString();
        note = in.readString();
        id = in.readInt();
    }


    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Task{" + "dateTime = '" + dateTime + '\'' + ",note = '" + note + '\'' + ",id = '" + id + '\'' + "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(dateTime);
        dest.writeString(note);
        dest.writeInt(id);
    }
}