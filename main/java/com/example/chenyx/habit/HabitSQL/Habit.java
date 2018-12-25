package com.example.chenyx.habit.HabitSQL;

import android.content.Context;
import android.os.Parcelable;

import org.parceler.Parcel;



public class Habit   {
    public long habit_id;
    public String habit_name;  //习惯名
    public String content; //提醒内容
    public int finish_frequent;    //每?天完成一次
    //public int times;  //已完成总次数
    public boolean remind;    //是否提醒
    public int remind_hour;    //提醒时
    public int remind_minute;  //提醒分
    //public int remind_times;   //差?天提醒,用每?天完成一次的次数初始化
    public String table_name;

    HabitManager myhabitmanager;    //日期表操作类

    public Habit(long habit_id,String habit_name,String content,
                 int finish_frequent, boolean remind, int remind_hour,
                 int remind_minute,String table_name, Context context) {
        this.habit_id=habit_id;
        this.content=content;
        this.habit_name = habit_name;
        this.finish_frequent = finish_frequent;
        this.remind = remind;
        if (remind == true) {
            this.remind_hour = remind_hour;
            this.remind_minute = remind_minute;
        } else {
            this.remind_hour = 0;
            this.remind_minute = 0;
        }
        //times = 0;
        //remind_times=finish_frequent;
        myhabitmanager=new HabitManager(context,table_name);
    }

    public Habit(Context context){
        //times = 0;
        //remind_times=finish_frequent;
        String table_name = "mydatesql" + (int)(1+Math.random()*10000);
        this.table_name = table_name;
        myhabitmanager=new HabitManager(context,table_name);
    }

    public long getHabit_id(){
        return habit_id;
    }

    public String getHabit_name(){
        return habit_name;
    }

//    public HabitManager getMyhabitmanager() {
//        return myhabitmanager;
//    }

    public HabitManager get_manager()
    {
        return myhabitmanager;
    }
}
