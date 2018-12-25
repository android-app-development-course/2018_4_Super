package com.example.chenyx.habit.HabitSQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;

import org.parceler.Parcel;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HabitManager   //日期数据库操作类
{
    //List<SignData> SignDataList;   //签到日期
    SignDataHelper mysigndatahelper;

    public HabitManager(Context context, String table_name)
    {
        //SignDataList = new ArrayList<SignData>();
        mysigndatahelper=new SignDataHelper(context,table_name);
    }

    public Boolean find(int year,int month,int day) //在数据中查找某天
    {
        SQLiteDatabase db=mysigndatahelper.getReadableDatabase();
        Cursor  cursor=db.query("signdata",null,"year=? and month=? and day=?",new String[]{""+year,""+month,""+day},null,null,null);
        boolean result=cursor.moveToNext();
        cursor.close();
        db.close();
        return result;
    }

    public int get_max_sign_day(int year,int month,int day) //查找某天的最大连签时间
    {
        SQLiteDatabase db=mysigndatahelper.getReadableDatabase();
        Cursor  cursor=db.query("signdata",null,"year=? and month=? and day=?",new String[]{""+year,""+month,""+day},null,null,null);
        cursor.moveToFirst();
        int max_sign_day=Integer.parseInt(cursor.getString(4));
        cursor.close();
        db.close();
        return max_sign_day;
    }

    public void update(int year,int month,int day) //将某天最长连签时间+1
    {
        int max_sign_day=get_max_sign_day(year,month,day)+1;
        SQLiteDatabase db=mysigndatahelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("max_sign_day",max_sign_day);
        db.update("signdata",values,"year=? and month=? and day=?",new String[]{""+year,""+month,""+day});
        db.close();
    }

    public long insert() //自动插入当前日期
    {
        Calendar calendar = Calendar.getInstance();//获取系统的日期
        int year = calendar.get(Calendar.YEAR); //年
        int month = calendar.get(Calendar.MONTH)+1;//月
        int day = calendar.get(Calendar.DAY_OF_MONTH);//日
        calendar.add( Calendar. DATE, -1);  //昨天
        int last_year=calendar.get(Calendar.YEAR); //年
        int last_month = calendar.get(Calendar.MONTH)+1;//月
        int last_day = calendar.get(Calendar.DAY_OF_MONTH);//日
        int max_sign_day;
        if (find(last_year,last_month,last_day)==true)
        {
            max_sign_day=get_max_sign_day(last_year,last_month,last_day)+1;
        }
        else
        {
            max_sign_day=1;
        }
        SQLiteDatabase db=mysigndatahelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("year",year);
        values.put("month",month);
        values.put("day",day);
        values.put("max_sign_day",max_sign_day);
        long id=db.insert("signdata",null,values);
        db.close();
        //若在某些天之前加了一天,后面连续的天连签日期全部+1
        calendar.add( Calendar. DATE, 2);  //明天
        int before_year=calendar.get(Calendar.YEAR); //年
        int before_month = calendar.get(Calendar.MONTH)+1;//月
        int before_day = calendar.get(Calendar.DAY_OF_MONTH);//日
        while (find(before_year,before_month,before_day)==true)
        {
            update(before_year,before_month,before_day);
            calendar.add( Calendar. DATE, 1);  //往前
            before_year=calendar.get(Calendar.YEAR); //年
            before_month = calendar.get(Calendar.MONTH)+1;//月
            before_day = calendar.get(Calendar.DAY_OF_MONTH);//日
        }
        return id;  //返回插入数据库中的habit_id
    }

    public long insert(int year,int month,int day)  //手动插入某天
    {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String dstr=year+"-"+month+"-"+day;
        Date date= null;
        try {
            date = sdf.parse(dstr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        cal.add( Calendar. DATE, -1);  //昨天
        int last_year=cal.get(Calendar.YEAR); //年
        int last_month = cal.get(Calendar.MONTH)+1;//月
        int last_day = cal.get(Calendar.DAY_OF_MONTH);//日
        int max_sign_day;
        if (find(last_year,last_month,last_day)==true)
        {
            max_sign_day=get_max_sign_day(last_year,last_month,last_day)+1;
        }
        else
        {
            max_sign_day=1;
        }
        SQLiteDatabase db=mysigndatahelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("year",year);
        values.put("month",month);
        values.put("day",day);
        values.put("max_sign_day",max_sign_day);
        long id=db.insert("signdata",null,values);
        db.close();
        //若在某些天之前加了一天,后面连续的天连签日期全部+1
        cal.add( Calendar. DATE, 2);  //明天
        int before_year=cal.get(Calendar.YEAR); //年
        int before_month = cal.get(Calendar.MONTH)+1;//月
        int before_day = cal.get(Calendar.DAY_OF_MONTH);//日
        while (find(before_year,before_month,before_day)==true)
        {
            update(before_year,before_month,before_day);
            cal.add( Calendar. DATE, 1);  //往前
            before_year=cal.get(Calendar.YEAR); //年
            before_month = cal.get(Calendar.MONTH)+1;//月
            before_day = cal.get(Calendar.DAY_OF_MONTH);//日
        }
        return id;  //返回插入数据库中的habit_id
    }

    public Cursor find_all()    //查找所有关系
    {   //返回光标
        SQLiteDatabase db=mysigndatahelper.getReadableDatabase();
        Cursor cursor=db.query("signdata",null,null,null,null,null,null);
        //db.close();
        return cursor;  //用完记得close
    }

    public void delete(long id)
    {   //按data_id删除某天
        SQLiteDatabase db=mysigndatahelper.getWritableDatabase();
        db.delete("signdata","data_id=?",new String[]{""+id});
        db.close();
    }

    public void delete(int year,int month,int day)
    {   //按日期删除某天
        SQLiteDatabase db=mysigndatahelper.getWritableDatabase();
        db.delete("signdata","year=? and month=? and day=?",new String[]{""+year,""+month,""+day});
        db.close();
    }

}