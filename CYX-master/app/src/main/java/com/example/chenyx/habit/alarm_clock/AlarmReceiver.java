package com.example.chenyx.habit.alarm_clock;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.chenyx.habit.MainActivity;
import com.example.chenyx.habit.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    private NotificationManager manager;
    private static final int NOTIFICATION_ID_1 = 0x00113;
    private String title;
    private String content = "提醒的时间到啦，快看看你要做的事...";


    private static final int PUSH_NOTIFICATION_ID = (0x001);
    private static final String PUSH_CHANNEL_ID = "PUSH_NOTIFY_ID";
    private static final String PUSH_CHANNEL_NAME = "PUSH_NOTIFY_NAME";

    @Override
    public void onReceive(final Context context, final Intent intent) {
//此处接收闹钟时间发送过来的广播信息，为了方便设置提醒内容
        //Log.v("gettest","get!!!");
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        //Toast.makeText(context,"123", Toast.LENGTH_LONG).show();
        showNormal(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, AlarmService.class);
        new Thread(new Runnable(){
            public void run(){
                try {
                    Thread.sleep(61000);
                }catch (InterruptedException e){
                }
                //handler.sendMessage(); //告诉主线程执行任务
                context.startService(intent);  //回调Service,同一个Service只会启动一个，所以直接再次启动Service，会重置开启新的提醒，
            }
        }).start();
    }

    /**
     * 发送通知
     */


    private void showNormal(Context context) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(PUSH_CHANNEL_ID, PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,PUSH_CHANNEL_ID);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        builder.setContentTitle(title)//设置通知栏标题
                .setSmallIcon(R.mipmap.assist_9)    //设置通知图标。
                .setContentIntent(pendingIntent) //设置通知栏点击意图
                .setContentText(content)
                .setTicker(content) //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setSmallIcon(R.mipmap.ic_launcher)//设置通知小ICON
                .setAutoCancel(true)//设置自动删除，点击通知栏信息之后系统会自动将状态栏的通知删除
                .setChannelId(PUSH_CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        if (notificationManager != null) {
            notificationManager.notify(PUSH_NOTIFICATION_ID, notification);
        }
//        Intent intent = new Intent(context, MainActivity.class);//这里是点击Notification 跳转的界面，可以自己选择
//        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification = new NotificationCompat.Builder(context)
//                .setSmallIcon(R.mipmap.assist_9)     //设置通知图标。
//                .setTicker(content)        //通知时在状态栏显示的通知内容
//                .setContentInfo("便签提醒")        //内容信息
//                .setContentTitle(title)        //设置通知标题。
//                .setContentText(content)        //设置通知内容。
//                .setAutoCancel(true)                //点击通知后通知消失
//                .setDefaults(Notification.DEFAULT_ALL)        //设置系统默认的通知音乐、振动、LED等。
//                .setPriority(NotificationCompat.PRIORITY_MAX)
//                .setContentIntent(pi)
//                .build();
//        manager.notify(NOTIFICATION_ID_1, notification);
    }

    //开机广播接收
    public class BootReceiver extends BroadcastReceiver {
        public BootReceiver() {
        }

        private final String ACTION = "android.intent.action.BOOT_COMPLETED";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION)) {
                Intent inten2 = new Intent(context, AlarmService.class);
                context.startService(inten2);
            }
            boolean isServiceRunning = false;
            if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                //检查Service状态
                ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                    if ("so.xxxx.xxxxService".equals(service.service.getClassName())) {
                        isServiceRunning = true;
                    }
                }
                if (!isServiceRunning) {
                    Intent i = new Intent(context, AlarmService.class);
                    context.startService(i);
                }
            }
        }
    }
}
