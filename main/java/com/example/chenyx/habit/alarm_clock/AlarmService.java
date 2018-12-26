package com.example.chenyx.habit.alarm_clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.chenyx.habit.HabitSQL.HabitHepler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmService extends Service {
    private AlarmManager am;
    private PendingIntent pi;
    private int hour;
    private int minute;
    //private String date;
    private String content;
    private String title;
    private HabitHepler mRemindSQL;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            getAlarmTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return START_REDELIVER_INTENT;
    } //这里为了提高优先级，选择START_REDELIVER_INTENT 没那么容易被内存清理时杀死
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public void getAlarmTime() throws ParseException {

        final Calendar mCalendar=Calendar.getInstance();
        long time=System.currentTimeMillis();
        mCalendar.setTimeInMillis(time);
        int mHour=mCalendar.get(Calendar.HOUR_OF_DAY);
        int mMinuts=mCalendar.get(Calendar.MINUTE);
//        Date mDate = new Date();
//        DateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd");
        int checkedId=-1,repeat=-1;

        mRemindSQL= new HabitHepler(this);
        SQLiteDatabase db = mRemindSQL.getWritableDatabase();
        Cursor cursor = db.query("habit", null, null, null, null, null, null);
        if (cursor.moveToFirst())
        { //遍历数据库的表，拿出一条，选择最近的时间赋值，作为第一条提醒数据。
            hour = 25;
            minute=61;
            do {
                //if(cursor.getInt(cursor.getColumnIndex("repeat"))==-1) continue;
                //if(cursor.getString(cursor.getColumnIndex("date"))==null) continue;
                //int habitId=cursor.getInt(cursor.getColumnIndex("habitID"));
                //int CountDay=0;
                //date=cursor.getString(cursor.getColumnIndex("date"));
                //Date d1=mSdf.parse(date);
                //int n=(int)(d1.getTime()-mDate.getTime())/(60*60*1000*24);
                //CountDay-=n;
                //if(CountDay<0) CountDay=0;
                //date=mSdf.format(mDate);
                //db.execSQL("update remain_data set CountDay=? and date=? where habitID=?",new Object[]{CountDay,date,habitId});
                ContentValues values = new ContentValues();
                //values.put("CountDay", CountDay);
                //values.put("date", date);
                //db.update("Remind_data",values,"habitID=?",new String[]{Integer.toString(checkedId)});
                if (hour >= cursor.getInt(cursor.getColumnIndex("remind_hour"))&&cursor.getInt(cursor.getColumnIndex("remind_hour"))>=mHour) {//&&CountDay==0
                    if (cursor.getInt(cursor.getColumnIndex("remind_minute")) >= mMinuts && minute >= cursor.getInt(cursor.getColumnIndex("remind_minute"))) {
                        hour = cursor.getInt(cursor.getColumnIndex("remind_hour"));
                        title = cursor.getString(cursor.getColumnIndex("habit_name"));
                        content = cursor.getString(cursor.getColumnIndex("content"));
                        minute = cursor.getInt(cursor.getColumnIndex("remind_minute"));
                        checkedId=cursor.getInt(cursor.getColumnIndex("habit_id"));
                        //repeat=cursor.getInt(cursor.getColumnIndex("repeat"));
                    }
                }
            } while (cursor.moveToNext());
//            if(checkedId!=-1)
//            {
//                ContentValues values = new ContentValues();
//                //if(repeat==0) repeat=-1;
//                //values.put("CountDay", repeat);
//                db.update("habit",values,"habit_id=?",new String[]{Integer.toString(checkedId)});
//            }
        }
        else {
            hour = 25;
        }
        cursor.close();//记得关闭游标，防止内存泄漏
        if(hour!=25) {
           // db.delete("Remind_data", "remindTime=?", new String[]{String.valueOf(time)});      //删除已经发送提醒的时间
            //Log.v("timetest2",""+time);
            Intent startNotification = new Intent(this, AlarmReceiver.class);   //这里启动的广播，下一步会教大家设置
            startNotification.putExtra("title",title);
            startNotification.putExtra("content", content);
            am = (AlarmManager) getSystemService(ALARM_SERVICE);   //这里是系统闹钟的对象
            pi = PendingIntent.getBroadcast(this, 0, startNotification, PendingIntent.FLAG_UPDATE_CURRENT);
            Calendar instance = Calendar.getInstance();
            instance.set(Calendar.HOUR_OF_DAY, hour);//小时
            instance.set(Calendar.MINUTE,minute);//分钟
            instance.set(Calendar.SECOND, 0);//秒//设置事件
            am.set(AlarmManager.RTC_WAKEUP, instance.getTimeInMillis(), pi);    //提交事件，发送给 广播接收器
        } else {
            //当提醒时间为空的时候，关闭服务，下次添加提醒时再开启
            stopService(new Intent(this, AlarmService.class));
        }
    }
}

