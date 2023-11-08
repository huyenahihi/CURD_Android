package com.example.ja2.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Observation implements Parcelable { //cho phép truyền dữ liệu đi

    public static final String DATA_OBSERVATION = "DATA_OBSERVATION";
    public static final String TABLE_NAME = "tbl_observation";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_HIKE_ID = "hike_id";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_DATE_TIME = "date_time";

    public static final Creator<Observation> CREATOR = new Creator<Observation>() {
        @Override
        public Observation createFromParcel(Parcel in) {
            return new Observation(in);
        }

        @Override
        public Observation[] newArray(int size) {
            return new Observation[size];
        }
    };

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NOTE + " TEXT,"
                    + COLUMN_DATE_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + COLUMN_HIKE_ID + " INTEGER, "
                    + "FOREIGN KEY(" + COLUMN_HIKE_ID + ") REFERENCES " + Hike.TABLE_NAME + "("+ Hike.COLUMN_ID+")"
                    + ")"; //tạo bảng
    private long dateTime;
    private String note;
    private int uid;
    private int id;

    public Observation() {

    }

    public Observation(long dateTime, String note, int uid) {
        this.dateTime = dateTime;
        this.note = note;
        this.uid = uid;
    }

    public Observation(long dateTime, String note, int uid, int id) {
        this.dateTime = dateTime;
        this.note = note;
        this.uid = uid;
        this.id = id;
    }

    protected Observation(Parcel in) {
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