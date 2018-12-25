package com.example.chenyx.habit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.MenuItemHoverListener;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chenyx.habit.HabitSQL.Habit;
import com.example.chenyx.habit.HabitSQL.Habit_All;
import com.example.chenyx.habit.HabitSQL.MyBaseAdapter;
import com.example.chenyx.habit.HabitSQL.SignData;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class item_intent extends  AppCompatActivity  {
    private Toolbar mToolbar;
    private ListView mListView;
    MyBaseAdapter myBaseAdapter;
    List<SignData> signData_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_intent);
        Intent intent=getIntent();
        signData_list=(ArrayList<SignData>)intent.getSerializableExtra("list");
        myBaseAdapter=new MyBaseAdapter(this,signData_list);
        init();
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
                break;
            case R.id.action_delete:
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void init()
    {
        mListView=(ListView)findViewById(R.id.listview);
        mListView.setAdapter(myBaseAdapter);
        mToolbar=(Toolbar)findViewById(R.id.toolbar2);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
}
