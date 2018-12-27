package com.example.chenyx.habit.HabitSQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.chenyx.habit.MainActivity;

import org.parceler.Parcel;

public class Habit_All
{   //习惯数据库操作类
    HabitHepler myhebithelper;

    public Habit_All(Context context)
    {
        myhebithelper=new HabitHepler(context);
    }

    public Habit_All(Habit_All habit_all)
    {
        myhebithelper=habit_all.getMyhebithelper();
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

    public long insert(final Habit habit)
    {
        SQLiteDatabase db=myhebithelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("habit_name",habit.habit_name);
        values.put("content",habit.content);
        values.put("finish_frequent",1);
        values.put("remind",habit.remind);
        values.put("remind_hour",habit.remind_hour);
        values.put("remind_minute",habit.remind_minute);
        values.put("remind_times",habit.finish_frequent);
        values.put("table_name",habit.table_name);
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

    public void update(Habit habit){
        SQLiteDatabase db=myhebithelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("habit_name",habit.habit_name);
        values.put("content",habit.content);
        values.put("finish_frequent",habit.finish_frequent);
        values.put("remind",habit.remind);
        values.put("remind_hour",habit.remind_hour);
        values.put("remind_minute",habit.remind_minute);
        values.put("remind_times",habit.finish_frequent);
        db.update("habit",values,"habit_id=?",new String[]{""+habit.habit_id});
        db.close();
    }

    public void delete(long id)
    {
        SQLiteDatabase db=myhebithelper.getWritableDatabase();
        db.delete("habit","habit_id=?",new String[]{""+id});
        db.close();
    }

    public HabitHepler getMyhebithelper() {
        return myhebithelper;
    }

//    public Cursor find_all(){
//
//        SQLiteDatabase db=myhebithelper.getReadableDatabase();
//        Cursor cursor=db.query("habit",null,null,null,null,null,null);
//        if (cursor.moveToFirst()){
//            do {
//                if (!cursor.getString(cursor.getColumnIndex("habit_name")).equals("")) {
//                    //Habit inserthabit = new Habit(MainActivity.this);
//                    Habit inserthabit= new Habit(cursor.getInt(cursor.getColumnIndex("habit_id")),
//                            cursor.getString(cursor.getColumnIndex("habit_name")),
//                            cursor.getString(cursor.getColumnIndex("content")),
//                            cursor.getInt(cursor.getColumnIndex("finish_frequent")),
//                            true,
//                            //cursor.getInt(cursor.getColumnIndex("times")),
//                            cursor.getInt(cursor.getColumnIndex("remind_hour")),
//                            cursor.getInt(cursor.getColumnIndex("remind_minute")),
//                            //cursor.getInt(cursor.getColumnIndex("remind_times")),
//                            cursor.getString(cursor.getColumnIndex("table_name")),
//                            MainActivity.this
//                    );
//
////                    inserthabit.habit_id = cursor.getInt(cursor.getColumnIndex("habit_id"));
////                    inserthabit.habit_name = cursor.getString(cursor.getColumnIndex("habit_name"));
////                    inserthabit.content = cursor.getString(cursor.getColumnIndex("content"));
////                    inserthabit.finish_frequent = cursor.getInt(cursor.getColumnIndex("finish_frequent"));
////                    inserthabit.times = cursor.getInt(cursor.getColumnIndex("times"));
////                    inserthabit.remind_hour = cursor.getInt(cursor.getColumnIndex("remind_hour"));
////                    inserthabit.remind_minute = cursor.getInt(cursor.getColumnIndex("remind_minute"));
////                    inserthabit.remind_times = cursor.getInt(cursor.getColumnIndex("remind_times"));
//                    habitDatas.add(inserthabit);
//                    //poslist.add(poslist.size());
//                }
//            }while (cursor.moveToNext());
//        }
//    }
//
    public Cursor find_all()    //查找所有关系
    {   //返回光标
        SQLiteDatabase db=myhebithelper.getReadableDatabase();
        Cursor cursor=db.query("habit",null,null,null,null,null,null);
        //db.close();
        return cursor;  //用完记得close
    }

    public Habit find(long habit_id,Context context)
    {
        Habit habit;
        SQLiteDatabase db=myhebithelper.getReadableDatabase();
        Cursor cursor=db.query("habit",null,"habit_id=?",new String[]{""+habit_id},null,null,null);
        cursor.close();
        db.close();
        habit=new Habit(cursor.getInt(cursor.getColumnIndex("habit_id")),
                cursor.getString(cursor.getColumnIndex("habit_name")),
                cursor.getString(cursor.getColumnIndex("content")),
                cursor.getInt(cursor.getColumnIndex("finish_frequent")),
                true,
                //cursor.getInt(cursor.getColumnIndex("times")),
                cursor.getInt(cursor.getColumnIndex("remind_hour")),
                cursor.getInt(cursor.getColumnIndex("remind_minute")),
                //cursor.getInt(cursor.getColumnIndex("remind_times")),
                cursor.getString(cursor.getColumnIndex("table_name")),
                context
        );

        return habit;
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