package com.example.chenyx.habit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chenyx.habit.HabitSQL.Habit;
import com.xx.roundprogressbar.RoundProgressBar;

import java.util.ArrayList;

/**
 * Created by chenyx on 18-12-17.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context;
    //private RoundProgressBar mRpb;
    private ArrayList<Habit> list;
    private ButtonInterface buttonInterface;
    private OnItemClickLitener mOnItemClickLitener;

    public MyAdapter(Context context, ArrayList<Habit> list) {
        this.context = context;
        this.list = list;
//        int position=list.size();
//        for(int i=0;i<list.size();i++){
//            notifyItemInserted(position);
//        }

    }
    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        Button id_button;
        RoundProgressBar mRpb;
        LinearLayout habit_item;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.id_num);
            id_button = (Button) itemView.findViewById(R.id.id_button);
            mRpb=(RoundProgressBar) itemView.findViewById(R.id.rpb);
            habit_item =(LinearLayout) itemView.findViewById(R.id.habit_item);
        }
    }
    /**
     *按钮点击事件需要的方法
     */
    public void buttonSetOnclick(ButtonInterface buttonInterface){
        this.buttonInterface=buttonInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface ButtonInterface{
        void onclick( View view,int position);
    }


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public interface OnItemClickLitener {
        /*点击事件*/
        void onItemClick(View view, int position);
        /*长按事件*/
        void onItemLongClick(View view, int position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_home, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv.setText(list.get(position).getHabit_name());
        //holder.id_button.setText(list.get(position));
        holder.id_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonInterface!=null) {
//                  接口实例化后的而对象，调用重写后的方法
                    buttonInterface.onclick(v,position);
                }
                holder.id_button.setVisibility(View.GONE);
            }
        });
        holder.mRpb.setAnimationDuration(1000);
        holder.mRpb.setCurrentProgress(50);
        holder.mRpb.setMaxProgress(100);
        holder.habit_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickLitener.onItemClick(v, position);
            }
        });
        holder.habit_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mOnItemClickLitener.onItemLongClick(v, position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addData(Habit newhabit) {
        int position=list.size();
        list.add(position, newhabit);
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public ArrayList<Habit> getdata(){
        return list;
    }
//    public String getname(int position){
//        return list.get(position);
//    }
}
