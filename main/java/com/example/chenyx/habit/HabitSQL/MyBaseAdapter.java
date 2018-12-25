package com.example.chenyx.habit.HabitSQL;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chenyx.habit.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyBaseAdapter extends BaseAdapter {
    Context context;
    List<SignData> signData_list;
    List<Max_Sign> max_signs_list;

    public MyBaseAdapter(Context context,List<SignData> signData_list)
    {
        this.context=context;
        this.signData_list=signData_list;
        max_signs_list=new ArrayList<Max_Sign>();
        get_list();
    }

    public void get_list()  //每当打卡时重新调用,并刷新适配器
    {
        boolean judge=false;
        SignData start=null,end=null;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String dstr;
        Date date= null;
        Calendar cal=Calendar.getInstance();
        if (signData_list.isEmpty())
        {
            return;
        }
        //cursor.moveToFirst();
        int i=0;
        int sum=signData_list.size();
        SignData temp;
        while (i<sum)
        {
            temp=signData_list.get(i);
            if (judge==false)
            {
                start=new SignData(temp);
                end=start;
                judge=true;
                dstr=start.getYear()+"-"+start.getMonth()+"-"+start.getDay();
                try {
                    date = sdf.parse(dstr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                cal.setTime(date);
            }
            else
            {
                cal.add( Calendar. DATE, 1);  //往前一天
                if (temp.getYear()==cal.get(Calendar.YEAR)&&
                        (temp.getMonth()==cal.get(Calendar.MONTH)+1)&&
                        temp.getDay()==cal.get(Calendar.DAY_OF_MONTH))
                {
                    end=new SignData(temp);
                }
                else
                {
                    max_signs_list.add(new Max_Sign(start,end));
                    judge=false;
                    i--;
                }
            }
            i++;
        }
        max_signs_list.add(new Max_Sign(start,end));
    }

    public int get_max()    //最长连签天数
    {
        int max=max_signs_list.get(0).getEnd().getMax_sign_day();
        for (int i=1;i<max_signs_list.size();i++)
        {
            if (max_signs_list.get(i).getEnd().getMax_sign_day()>max)
            {
                max=max_signs_list.get(i).getEnd().getMax_sign_day();
            }
        }
        return max;
    }

    @Override
    public int getCount() {
        return max_signs_list.size();
    }

    @Override
    public Object getItem(int position) {
        return max_signs_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Max_Sign temp=(Max_Sign)getItem(position);
        ViewHolder holder;
        if (convertView==null)
        {
            convertView=LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false);
            holder=new ViewHolder();
            holder.mProgressBar=(ProgressBar)convertView.findViewById(R.id.PB);
            holder.mProgressBar.setMax(get_max());
            //holder.mProgressBar.setProgress(0);
            holder.mTextView=(TextView)convertView.findViewById(R.id.TV);
            convertView.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)convertView.getTag();
        }
        holder.mProgressBar.setProgress(temp.getEnd().getMax_sign_day());
        holder.mTextView.setText(temp.getStart().getYear()+"."+temp.getStart().getMonth()+"."+temp.getStart().getDay()+"-"+temp.getEnd().getYear()+"."+temp.getEnd().getMonth()+"."+temp.getEnd().getDay());
        return convertView;
    }

    class ViewHolder
    {
        ProgressBar mProgressBar;
        TextView mTextView;
    }

}
