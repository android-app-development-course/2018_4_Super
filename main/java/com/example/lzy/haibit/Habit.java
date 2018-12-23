package com.example.lzy.haibit;

import android.content.Context;

public class Habit {
    long habit_id;
    String habit_name;  //习惯名
    String content; //提醒内容
    int finish_frequent;    //每?天完成一次
    int times;  //已完成总次数
    boolean remind;    //是否提醒
    int remind_hour;    //提醒时
    int remind_minute;  //提醒分
    int remind_times;   //差?天提醒,用每?天完成一次的次数初始化

    HabitManager myhabitmanager;    //日期表操作类

    public Habit(long habit_id,String habit_name,String content, int finish_frequent, boolean remind, int remind_hour, int remind_minute, Context context,String table_name) {
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
        times = 0;
        remind_times=finish_frequent;
        myhabitmanager=new HabitManager(context,table_name);
    }

    public HabitManager get_manager()
    {
        return myhabitmanager;
    }

}
