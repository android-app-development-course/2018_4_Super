package com.example.chenyx.habit;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.chenyx.habit.alarm_clock.AlarmService;

import java.util.Calendar;

/**
 * Created by chenyx on 18-12-19.
 */

public class new_habit_Dialog extends Dialog {
    private String dialogName;
    private Button btnsave;
    private Button btncencel;
    private Button btntime;
    private EditText habitname;
    private EditText remindtext;
    private Context context;

    public new_habit_Dialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    public interface saveButtonListener {
        /**
         * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
         */
        void setActivityText(String name,String remind);    //,int finish_frequent,boolean remindornot,int remind_hour,int remind_minute
    }
    private saveButtonListener savelistener;

    public interface cancelButtonListener {
        void setActivity();
    }
    private cancelButtonListener cancellistener;

    public interface timeButtonListener {
        void setActivity();
    }
    private timeButtonListener timelistener;

    public void saveSetOnclick(saveButtonListener savelistener){
        this.savelistener=savelistener;
    }

    public void cancelSetOnclick(cancelButtonListener cancellistener){
        this.cancellistener=cancellistener;
    }

    public void timeSetOnclick(timeButtonListener timelistener){
        this.timelistener=timelistener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflate = LayoutInflater.from(context);
        View view = inflate.inflate(R.layout.new_habit, null);
        setContentView(view);

        btnsave = (Button) view.findViewById(R.id.btnsave);
        btncencel=(Button) view.findViewById(R.id.btncencel);
        btntime = (Button) view.findViewById(R.id.alarm_time);

        habitname=(EditText) view.findViewById(R.id.habitname);
        remindtext=(EditText) view.findViewById(R.id.remindtext);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!habitname.getText().toString().equals("")){
                savelistener.setActivityText(habitname.getText().toString(),
                        remindtext.getText().toString()
                        );
                }
                dismiss();
            }
        });

        btncencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancellistener.setActivity();
                dismiss();
            }
        });

        btntime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timelistener.setActivity();
            }
        });
    }



}
