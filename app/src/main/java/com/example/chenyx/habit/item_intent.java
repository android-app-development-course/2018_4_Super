package com.example.chenyx.habit;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.MenuItemHoverListener;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.chenyx.habit.HabitSQL.Habit;
import com.example.chenyx.habit.HabitSQL.Habit_All;
import com.example.chenyx.habit.HabitSQL.MyBaseAdapter;
import com.example.chenyx.habit.HabitSQL.SignData;
import com.example.chenyx.habit.HabitSQL.set;
import com.github.airsaid.calendarview.widget.CalendarView;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import lecho.lib.hellocharts.view.LineChartView;

public class item_intent extends  AppCompatActivity  {
    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;
    private ListView mListView;
    private  LineChartView lineChartView;
    MyBaseAdapter myBaseAdapter;
    List<SignData> signData_list;

    long habit_id;
    int pos;
    String habit_name;
    String content;
    boolean remind;
    int remind_hour;
    int remind_minute;

    private static final String TAG = MainActivity.class.getSimpleName();

    private CalendarView mCalendarView;
    private TextView mTxtDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_intent);
        Intent intent=getIntent();
        signData_list=(ArrayList<SignData>)intent.getSerializableExtra("list");
        habit_id = intent.getLongExtra("habit_id",0);
        pos = intent.getIntExtra("pos",0);
        habit_name = " ";
        content = " ";
        remind = false;
        remind_hour = 25;
        remind_minute = 61;
        myBaseAdapter=new MyBaseAdapter(this,signData_list);
        init();
        set s=new set(lineChartView,signData_list);
        s.start();

        mTxtDate = (TextView) findViewById(R.id.txt_date);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);

        mToolbar.setTitle(intent.getStringExtra("habit_name"));
        setSupportActionBar(mToolbar);
        // 设置已选的日期
        mCalendarView.setSelectDate(initData());

        // 指定显示的日期, 如当前月的下个月
        //Calendar calendar = mCalendarView.getCalendar();
       // calendar.add(Calendar.MONTH, 2);
       // mCalendarView.setCalendar(calendar);

        // 设置字体
        mCalendarView.setTypeface(Typeface.SERIF);

//        // 设置日期状态改变监听
//        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView view, boolean select, int year, int month, int day) {
//                Log.e(TAG, "select: " + select);
//                Log.e(TAG, "year: " + year);
//                Log.e(TAG, "month,: " + (month + 1));
//                Log.e(TAG, "day: " + day);
//
//                if(select){
//                    Toast.makeText(getApplicationContext()
//                            , "选中了：" + year + "年" + (month + 1) + "月" + day + "日", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(getApplicationContext()
//                            , "取消选中了：" + year + "年" + (month + 1) + "月" + day + "日", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        // 设置是否能够改变日期状态
        mCalendarView.setChangeDateStatus(false);

//        // 设置日期点击监听
//        mCalendarView.setOnDataClickListener(new CalendarView.OnDataClickListener() {
//            @Override
//            public void onDataClick(@NonNull CalendarView view, int year, int month, int day) {
//                Log.e(TAG, "year: " + year);
//                Log.e(TAG, "month,: " + (month + 1));
//                Log.e(TAG, "day: " + day);
//            }
//        });
        // 设置是否能够点击
        mCalendarView.setClickable(false);

        setCurDate();
    }


    void init()
    {
        mListView=(ListView)findViewById(R.id.listview);
        mListView.setAdapter(myBaseAdapter);
        setListViewHeightBasedOnChildren(mListView);
        mListView.setFocusable(false);
        mToolbar=(Toolbar)findViewById(R.id.toolbar2);
        mAppBarLayout=(AppBarLayout)findViewById(R.id.toolbar);
        lineChartView=(LineChartView)findViewById(R.id.line_chart);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
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
                        intent.putExtra("remind_hour", remind_hour);
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
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(item_intent.this);
                normalDialog.setTitle("删除");
                normalDialog.setMessage("确定删除?");
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //...To-do
                                Intent intent = new Intent();
                                intent.putExtra("habit_id", habit_id);
                                intent.putExtra("pos",pos);
                                setResult(2, intent);
                                finish();
                            }
                        });
                normalDialog.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                // 显示
                normalDialog.show();
//                Intent intent = new Intent();
//                intent.putExtra("habit_id", habit_id);
//                intent.putExtra("pos",pos);
//                setResult(2, intent);
//                finish();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private List<String> initData() {
        List<String> dates = new ArrayList<>();
        SignData temp;
        for (int  i=0;i<signData_list.size();i++) {
            temp = signData_list.get(i);
            if (temp.getDay() > 9) {
                dates.add("" + temp.getYear() + temp.getMonth() + temp.getDay());
            }
            else {
                dates.add("" + temp.getYear() + temp.getMonth() +"0"+ temp.getDay());
            }
        }

        return dates;
    }

    public void next(View v){
        mCalendarView.nextMonth();
        setCurDate();
    }

    public void last(View v){
        mCalendarView.lastMonth();
        setCurDate();
    }

    private void setCurDate(){
        mTxtDate.setText(mCalendarView.getYear() + "年" + (mCalendarView.getMonth() + 1) + "月");
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        try {
            // 获取ListView对应的Adapter
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                return;
            }

            int totalHeight = 0;
            for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
                // listAdapter.getCount()返回数据项的数目
                View listItem = listAdapter.getView(i, null, listView);
                // 计算子项View 的宽高
                listItem.measure(0, 0);
                // 统计所有子项的总高度
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            // listView.getDividerHeight()获取子项间分隔符占用的高度
            // params.height最后得到整个ListView完整显示需要的高度
            listView.setLayoutParams(params);
        } catch (Exception e) {
        }
    }
}
