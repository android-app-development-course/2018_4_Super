package com.example.chenyx.habit;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.example.chenyx.habit.HabitSQL.Habit;
import com.example.chenyx.habit.HabitSQL.HabitHepler;
import com.example.chenyx.habit.HabitSQL.Habit_All;
import com.example.chenyx.habit.HabitSQL.SignData;
import com.example.chenyx.habit.alarm_clock.AlarmService;

import org.parceler.Parcels;

public class MainActivity extends BaseActivity {
    private RecyclerView rv_falls ;
    private Button mbutton1;
    private Button mbutton2;
    private ToggleButton mbutton3;
    private ArrayList<Habit> habitDatas;
    private ArrayList<Integer> poslist;
    MyAdapter homeAdapter;
    private String name;
    private String remind;
    public Habit_All habit_all;
    private HabitHepler myhebithelper;
    private boolean alarmflag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        habit_all=new Habit_All(this);
        myhebithelper=new HabitHepler(this);

        rv_falls = (RecyclerView) findViewById(R.id.rv_falls);
        mbutton1 = (Button) findViewById(R.id.btn_one);
        mbutton2 = (Button) findViewById(R.id.btn_two);
        mbutton3 = (ToggleButton) findViewById(R.id.btn_three);
        alarmflag = true;

        rv_falls.setLayoutManager(new StaggeredGridLayoutManager(2,  StaggeredGridLayoutManager.VERTICAL));

        habitDatas = new ArrayList<>();
        poslist = new ArrayList<>();
        homeAdapter = new MyAdapter(MainActivity.this, habitDatas);
        HabitHepler habitHepler = habit_all.getMyhebithelper();
        SQLiteDatabase db=habitHepler.getReadableDatabase();
        Cursor cursor=db.query("habit",null,null,null,null,null,null);

        //初始读数据
        if (cursor.moveToFirst()){
            do {
                if (!cursor.getString(cursor.getColumnIndex("habit_name")).equals("")) {
                    //Habit inserthabit = new Habit(MainActivity.this);
                    Habit inserthabit= new Habit(cursor.getInt(cursor.getColumnIndex("habit_id")),
                            cursor.getString(cursor.getColumnIndex("habit_name")),
                            cursor.getString(cursor.getColumnIndex("content")),
                            cursor.getInt(cursor.getColumnIndex("finish_frequent")),
                            true,
                            //cursor.getInt(cursor.getColumnIndex("times")),
                            cursor.getInt(cursor.getColumnIndex("remind_hour")),
                            cursor.getInt(cursor.getColumnIndex("remind_minute")),
                            //cursor.getInt(cursor.getColumnIndex("remind_times")),
                            cursor.getString(cursor.getColumnIndex("table_name")),
                            MainActivity.this
                    );
                    habitDatas.add(inserthabit);
                    poslist.add(poslist.size());
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        rv_falls.setAdapter(homeAdapter);
        //长按拖拽
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemDragHelperCallBack(new ItemDragHelperCallBack.OnItemDragListener() {
            @Override
            public void onItemMove(int startPos, int endPos) {
                //交换变换位置的集合数据
                if (startPos < endPos){
                    for(int i=startPos;i<endPos;i++) {
                        Collections.swap(habitDatas, i, i+1);
                        Collections.swap(poslist, i, i+1);
                    }
                }
                else {
                    for(int i=startPos;i>endPos;i--) {
                        Collections.swap(habitDatas, i, i-1);
                        Collections.swap(poslist, i, i-1);
                    }
                }
                homeAdapter.notifyItemMoved(startPos, endPos);
            }
        }));
        //关联RecyclerView
        itemTouchHelper.attachToRecyclerView(rv_falls);

//      调用按钮返回事件回调的方法
        homeAdapter.buttonSetOnclick(new MyAdapter.ButtonInterface() {
            @Override
            public void onclick(View view,int position) {
                habitDatas.get(poslist.indexOf(position)).get_manager().insert();
                //homeAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "打卡成功", Toast.LENGTH_SHORT).show();
            }
        });

        homeAdapter.setOnItemClickLitener(new MyAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Habit habit=habitDatas.get(poslist.indexOf(position));
                Cursor cursor=habit.get_manager().find_all();
                List<SignData> signData_list=new ArrayList<SignData>();
                while (cursor.moveToNext())
                {
                    signData_list.add(new SignData(Long.parseLong(cursor.getString(0)),
                            Integer.parseInt(cursor.getString(1)),
                            Integer.parseInt(cursor.getString(2)),
                            Integer.parseInt(cursor.getString(3)),
                            Integer.parseInt(cursor.getString(4))));
                }
                Intent intent=new Intent(view.getContext(),item_intent.class);
                intent.putExtra("list",(Serializable)signData_list);
                intent.putExtra("habit_id",habit.getHabit_id());
                intent.putExtra("pos",poslist.indexOf(position));
                startActivityForResult(intent,1);
                //Toast.makeText(MainActivity.this, ""+habitDatas.get(poslist.indexOf(position)).getHabit_id(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });


        mbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new_habit_Dialog newhabit=new new_habit_Dialog(MainActivity.this);
                final Habit habit=new Habit(MainActivity.this);
                newhabit.saveSetOnclick(new new_habit_Dialog.saveButtonListener() {
                    @Override
                    public void setActivityText(String name, String remind) {
                        MainActivity.this.name=name;
                        MainActivity.this.remind=remind;

                        habit.habit_name=name;
                        habit.content=remind;
                        habit.finish_frequent = 1;
                        habitDatas.add(habit);
                        poslist.add(poslist.size());
                        habit_all.insert(habit);    //插入数据库
                        //homeAdapter.addData(habit);
                        if(alarmflag) {
                            start();
                        }
                        //homeAdapter.notifyDataSetChanged();
                        menu.dismiss();
                    }
                });
                newhabit.cancelSetOnclick(new new_habit_Dialog.cancelButtonListener() {
                    @Override
                    public void setActivity() {
                        menu.dismiss();
                    }
                });
                newhabit.timeSetOnclick(new new_habit_Dialog.timeButtonListener() {
                    @Override
                    public void setActivity() {
                        Calendar c = Calendar.getInstance();
                        showTimePickerDialog(MainActivity.this,c,habit,0);
                    }
                });
                newhabit.show();
            }
        });

        mbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                if (habitDatas.size()==0){
                    Toast.makeText(MainActivity.this, "目前没有未打卡的习惯", Toast.LENGTH_SHORT).show();
                }
                else {
                    showTimePickerDialog(MainActivity.this,c,habitDatas.get(0),1);
                }
                //showTimePickerDialog(MainActivity.this,c,habit);
            }
        });

        mbutton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    MainActivity.this.alarmflag=true;
                    start();
                    Toast.makeText(MainActivity.this, "打开", Toast.LENGTH_SHORT).show();
                }else {
                    MainActivity.this.alarmflag=false;
                    stop();
                    Toast.makeText(MainActivity.this, "关闭", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    //弹出时间  选择提醒时间 这里只设置时间 不设置重复
    public void showTimePickerDialog(Activity activity, Calendar calendar, final Habit habit, int flag) {
        // Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来
        // 解释一哈，Activity是context的子类
        if (flag == 0) {
            new TimePickerDialog(activity,
                    // 绑定监听器
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            habit.remind_hour = hourOfDay;
                            habit.remind_minute = minute;
                            habit.remind = true;
                            Calendar cal = Calendar.getInstance();
//                        String date=cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE);
//                        habit.date=date;
                            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            //long triggerAtTime=cal.getTimeInMillis()+hourOfDay*60*60*1000+minute*60*1000;
                        }
                    }
                    // 设置初始时间
                    , calendar.get(Calendar.HOUR_OF_DAY)
                    , calendar.get(Calendar.MINUTE)
                    // true表示采用24小时制
                    , true).show();
        }
        else {
            new TimePickerDialog(activity,
                    // 绑定监听器
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            habit.remind_hour = hourOfDay;
                            habit.remind_minute = minute;
                            habit.remind = true;
                            Calendar cal = Calendar.getInstance();
//                        String date=cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE);
//                        habit.date=date;
                            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            habit_all.update(habit.habit_id,
                                    habit.habit_name,
                                    habit.content,
                                    1,
                                    true,
                                    habit.remind_hour,
                                    habit.remind_minute
                            );
                            if(alarmflag) {
                                start();
                            }
                            menu.dismiss();
                        }
                    }
                    // 设置初始时间
                    , calendar.get(Calendar.HOUR_OF_DAY)
                    , calendar.get(Calendar.MINUTE)
                    // true表示采用24小时制
                    , true).show();
        }
    }


    public void start()
    {
        Intent intent=new Intent(this,AlarmService.class);
        startService(intent);
    }

    public void stop(){
        Intent intent=new Intent(this,AlarmService.class);
        stopService(intent);
    }

    @Override
    // 当结果返回后判断并执行操作
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 1) {
                if (resultCode == 1) {
                    habit_all.update(data.getLongExtra("habit_id",0),
                            data.getStringExtra("habit_name"),
                            data.getStringExtra("content"),
                            1,
                            true,
                            data.getIntExtra("remind_hour",25),
                            data.getIntExtra("remind_minute",61)
                    );
                    habitDatas.get(data.getIntExtra("pos",0)).habit_name = data.getStringExtra("habit_name");
                    habitDatas.set(data.getIntExtra("pos",0),
                            habitDatas.get(data.getIntExtra("pos",0))
                    );

                    homeAdapter.notifyDataSetChanged();
                    Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
                }
                if (resultCode == 2) {
                    habit_all.delete(data.getLongExtra("habit_id",0));
                    getApplicationContext().deleteDatabase(habitDatas.get(data.getIntExtra("pos",0)).table_name);
                    habitDatas.remove(data.getIntExtra("pos",0));
                    //homeAdapter.list.remove(data.getIntExtra("pos",0));
                    homeAdapter.notifyDataSetChanged();

                    Toast.makeText(this,"删除成功",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }











    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
