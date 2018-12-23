package com.example.lzy.haibit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SignDataHelper extends SQLiteOpenHelper    //允许在表中添加列,不建议在此类中添加函数
{   //一个习惯对应一个签到表
    String table_name;

    SignDataHelper(Context context,String table_name)
    {
        super(context,"signdata.db",null,1);
        this.table_name=table_name;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+table_name+"(data_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "year INTEGER," +
                "month INTEGER," +
                "day INTEGER," +
                "max_sign_day INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
