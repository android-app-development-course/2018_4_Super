package com.example.lzy.haibit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Habit_All
{   //习惯数据库操作类
    HabitHepler myhebithelper;

    public Habit_All(Context context)
    {
        myhebithelper=new HabitHepler(context);
    }

    public long insert(String habit_name,String content, int finish_frequent, boolean remind, int remind_hour, int remind_minute)
    {
        SQLiteDatabase db=myhebithelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("habit_name",habit_name);
        values.put("content",content);
        values.put("finish_frequent",finish_frequent);
        values.put("remind",remind);
        values.put("remind_hour",remind_hour);
        values.put("remind_minute",remind_minute);
        values.put("remind_times",finish_frequent);
        long id=db.insert("habit",null,values);
        db.close();
        return id;  //反回插入的主键id
    }

    public void update(long id,String habit_name,String content, int finish_frequent, boolean remind, int remind_hour, int remind_minute)
    {   //用id找到要修改的关系
        SQLiteDatabase db=myhebithelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("habit_name",habit_name);
        values.put("content",content);
        values.put("finish_frequent",finish_frequent);
        values.put("remind",remind);
        values.put("remind_hour",remind_hour);
        values.put("remind_minute",remind_minute);
        values.put("remind_times",finish_frequent);
        db.update("habit",values,"habit_id=?",new String[]{""+id});
        db.close();
    }

    public void delete(long id)
    {
        SQLiteDatabase db=myhebithelper.getWritableDatabase();
        db.delete("habit","habit_id=?",new String[]{""+id});
        db.close();
    }

    public Cursor find_all()    //查找所有关系
    {   //返回光标
        SQLiteDatabase db=myhebithelper.getReadableDatabase();
        Cursor cursor=db.query("habit",null,null,null,null,null,null);
        db.close();
        return cursor;  //用完记得close
    }

    public void sub_remind_times(long id)   //提醒时间减一天
    {
        SQLiteDatabase db=myhebithelper.getWritableDatabase();
        Cursor cursor=db.query("habit",new String[]{"habit_id","finish_frequent","remind_times"},"habit_id=?",new String[]{""+id},null,null,null);
        cursor.moveToFirst();
        int finish_frequent=Integer.parseInt(cursor.getString(1));
        int remind_times=Integer.parseInt(cursor.getString(2));
        cursor.close();
        remind_times--;
        if (remind_times<=0)
        {
            remind_times=finish_frequent;
        }
        ContentValues values=new ContentValues();
        values.put("remind_times",remind_times);
        db.update("habit",values,"habit_id=?",new String[]{""+id});
        db.close();
    }
}