package com.example.lzy.haibit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView=(ListView)findViewById(R.id.mlistview);
        Habit habit=new Habit(1,"eat","eat",1,false,0,0,this,"test");
        habit.get_manager().insert(2018,12,18);
        habit.get_manager().insert(2018,12,19);
        habit.get_manager().insert(2018,12,21);
        habit.get_manager().insert(2018,12,22);
        MyBaseAdapter adapter=new MyBaseAdapter(this,habit);
        mListView.setAdapter(adapter);
        habit.get_manager().insert();
        adapter.notifyDataSetChanged();
    }
}
