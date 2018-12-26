package com.example.chenyx.habit.HabitSQL;

import android.os.Parcelable;

import org.parceler.Parcel;

import java.io.Serializable;

public class SignData implements Parcelable,Serializable {
    long data_id;
    int year;
    int month;
    int day;
    int max_sign_day;   //最长签到时间

    public SignData(SignData sd)
    {
        this.data_id=sd.getData_id();
        this.year=sd.getYear();
        this.month=sd.getMonth();
        this.day=sd.getDay();
        this.max_sign_day=sd.getMax_sign_day();
    }

    public SignData(long data_id,int year,int month,int day,int max_sign_day)
    {
        this.data_id=data_id;
        this.year=year;
        this.month=month;
        this.day=day;
        this.max_sign_day=max_sign_day;
    }

    public long getData_id() {
        return data_id;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getMax_sign_day() {
        return max_sign_day;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeLong(this.data_id);
        dest.writeInt(this.year);
        dest.writeInt(this.month);
        dest.writeInt(this.day);
        dest.writeInt(this.max_sign_day);
    }

    protected SignData(android.os.Parcel in) {
        this.data_id = in.readLong();
        this.year = in.readInt();
        this.month = in.readInt();
        this.day = in.readInt();
        this.max_sign_day = in.readInt();
    }

    public static final Parcelable.Creator<SignData> CREATOR = new Parcelable.Creator<SignData>() {
        @Override
        public SignData createFromParcel(android.os.Parcel source) {
            return new SignData(source);
        }

        @Override
        public SignData[] newArray(int size) {
            return new SignData[size];
        }
    };
}