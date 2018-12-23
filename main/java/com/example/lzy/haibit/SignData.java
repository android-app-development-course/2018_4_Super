package com.example.lzy.haibit;

public class SignData {
    long data_id;
    int year;
    int month;
    int day;
    int max_sign_day;   //最长签到时间

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
}
