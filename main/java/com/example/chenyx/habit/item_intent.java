package com.example.chenyx.habit;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.MenuItemHoverListener;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.chenyx.habit.HabitSQL.Habit;
import com.example.chenyx.habit.HabitSQL.Habit_All;
import com.example.chenyx.habit.HabitSQL.SignData;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class item_intent extends  AppCompatActivity  {
    private Toolbar mToolbar;
    private ListView mListView;
    MyBaseAdapter myBaseAdapter;
    List<SignData> signData_list;
    int habit_id;
    int pos;
    String habit_name;
    String content;
    boolean remind;
    int remind_hour;
    int remind_minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_intent);
        Intent intent=getIntent();
        signData_list=(ArrayList<SignData>)intent.getSerializableExtra("list");
        myBaseAdapter=new MyBaseAdapter(this,signData_list);
        init();
    }

    void init()
    {
        mListView=(ListView)findViewById(R.id.listview);
        mListView.setAdapter(myBaseAdapter);
        mToolbar=(Toolbar)findViewById(R.id.toolbar2);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        Intent intent = getIntent();
        habit_id = intent.getIntExtra("habit_id",0);
        pos = intent.getIntExtra("pos",0);
        habit_name = " ";
        content = " ";
        remind = false;
        remind_hour = 25;
        remind_minute = 61;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update:
                //弹出时间  选择提醒时间 这里只设置时间 不设置重复
                new_habit_Dialog newhabit=new new_habit_Dialog(item_intent.this);
                newhabit.saveSetOnclick(new new_habit_Dialog.saveButtonListener() {
                    @Override
                    public void setActivityText(String name, String cont) {
                        habit_name=name;
                        content=cont;
                        Intent intent = new Intent();
                        intent.putExtra("habit_id", habit_id);
                        intent.putExtra("habit_name", habit_name);
                        intent.putExtra("content", content);
                        intent.putExtra("remind_hour", habit_name);
                        intent.putExtra("remind_minute", remind_minute);
                        intent.putExtra("remind", remind);
                        intent.putExtra("pos",pos);
                        setResult(1, intent);
                        finish();
                    }
                });
                newhabit.cancelSetOnclick(new new_habit_Dialog.cancelButtonListener() {
                    @Override
                    public void setActivity() {
                    }
                });
                newhabit.timeSetOnclick(new new_habit_Dialog.timeButtonListener() {
                    @Override
                    public void setActivity() {
                        new TimePickerDialog(item_intent.this,
                                // 绑定监听器
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        remind_hour = hourOfDay;
                                        remind_minute = minute;
                                        remind=true;
                                    }
                                }
                                // 设置初始时间
                                , 0
                                , 0
                                // true表示采用24小时制
                                , true).show();
                    }
                });
                //newhabit.habitname.setText(habit_name.toCharArray(), 0, habit_name.length());
                newhabit.show();
                break;
            case R.id.action_delete:
                Intent intent = new Intent();
                intent.putExtra("habit_id", habit_id);
                intent.putExtra("pos",pos);
                setResult(2, intent);
                finish();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
