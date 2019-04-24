package com.helin.accountingapp.set;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.helin.accountingapp.MainActivity;
import com.helin.accountingapp.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by helin on 2019/4/18
 */
public class PushService extends Service {
    static Timer timer = null;

    String name = "account";//渠道名字
    String id = "account"; // 渠道ID
    String description = "account  notification"; // 渠道解释说明
    PendingIntent pendingIntent;//非紧急意图，可设置可不设置


    //清除通知
    public static void cleanAllNotification() {
        NotificationManager mn = (NotificationManager) SetMainActivity.getContext().getSystemService(NOTIFICATION_SERVICE);
        mn.cancelAll();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    //添加通知
    public static void addNotification(long delayTime, String tickerText, String contentTitle, String contentText) {
        Intent intent = new Intent(SetMainActivity.getContext(), PushService.class);
        intent.putExtra("delayTime", delayTime);
        intent.putExtra("tickerText", tickerText);
        intent.putExtra("contentTitle", contentTitle);
        intent.putExtra("contentText", contentText);
        SetMainActivity.getContext().startService(intent);
    }

    public void onCreate() {
        Log.e("addNotification", "===========create=======");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public int onStartCommand(final Intent intent, int flags, int startId) {

        long period = 24 * 60 * 60 * 1000; //24小时一个周期
        long delay = intent.getLongExtra("delayTime", 0);
        if (null == timer) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                // TODO Auto-generated method stub
                NotificationManager mn = (NotificationManager) PushService.this.getSystemService(NOTIFICATION_SERVICE);
                Notification.Builder builder = new Notification.Builder(PushService.this,id);
                Intent notificationIntent = new Intent(PushService.this, MainActivity.class);//点击跳转位置
                PendingIntent contentIntent = PendingIntent.getActivity(PushService.this, 0, notificationIntent, 0);
                builder.setContentIntent(contentIntent);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = null;
                    if (mChannel == null) {
                        mChannel = new NotificationChannel(id, name, importance);
                        mChannel.setDescription(description);
                        mChannel.enableLights(true);//是否在桌面icon右上角展示小红点
                        mn.createNotificationChannel(mChannel);
                    }
                    builder.setChannelId(id);
                }
                builder.setSmallIcon(R.drawable.norecord_icon)
                        .setTicker(intent.getStringExtra("tickerText")) //测试通知栏标题
                        .setContentText(intent.getStringExtra("contentText"))//下拉通知啦内容
                        .setContentTitle(intent.getStringExtra("contentTitle"))//下拉通知栏标题
                        .setAutoCancel(true);
                Notification notification = builder.build();
                mn.notify((int) System.currentTimeMillis(), notification);
            }
        }, delay, period);
        System.out.printf("=============="+delay);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e("addNotification", "===========destroy=======");
        super.onDestroy();
    }
}
