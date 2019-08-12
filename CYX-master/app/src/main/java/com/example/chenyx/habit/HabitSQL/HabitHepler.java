package com.example.chenyx.habit.HabitSQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.io.Serializable;

public class HabitHepler extends SQLiteOpenHelper   //允许在表中添加列,不建议在此类中添加函数
{
    public HabitHepler(Context context)
    {
        super(context,"habit.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE habit(" +
                "habit_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "habit_name VARCHAR(20)," +
                "content VARCHAR(50),"+
                "finish_frequent INTEGER," +
                "times INTEGER," +
                "remind BOOLEAN," +
                "remind_hour INTEGER," +
                "remind_minute INTEGER," +
                "table_name VARCHAR(50),"+
                "remind_times INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
