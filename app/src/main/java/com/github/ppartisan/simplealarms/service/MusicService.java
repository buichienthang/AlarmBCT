package com.github.ppartisan.simplealarms.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.github.ppartisan.simplealarms.R;
import com.github.ppartisan.simplealarms.model.Alarm;
import com.github.ppartisan.simplealarms.ui.showAlarmActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;
    private static final String BUNDLE_EXTRA = "bundle_extra";
    private static final String ALARM_KEY = "alarm_key";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Alarm alarm = null;

        try {
            //  Block of code to try
            alarm = intent.getBundleExtra(BUNDLE_EXTRA).getParcelable(ALARM_KEY);
        }
        catch(Exception e) {
            //  Block of code to handle errors
        }

        if (intent.getStringExtra("check_play") != null) {
            if (intent.getStringExtra("check_play").equals("on")) {
                mediaPlayer = MediaPlayer.create(this, R.raw.music_alarm);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();

                if (Build.VERSION.SDK_INT >= 26) {
                    String CHANNEL_ID = "my_channel_01";
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                            "Channel human readable title",
                            NotificationManager.IMPORTANCE_HIGH);

                    ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

                    Intent fullScreenIntent = new Intent(this, showAlarmActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(ALARM_KEY, alarm);
                    intent.putExtra(BUNDLE_EXTRA, bundle);
                    PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                            fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder notificationBuilder =
                            new NotificationCompat.Builder(this, CHANNEL_ID)
                                    .setSmallIcon(R.drawable.ic_alarm)
                                    .setContentTitle("Incoming call")
                                    .setContentText("(919) 555-1234")
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setCategory(NotificationCompat.CATEGORY_CALL)

                                    // Use a full-screen intent only for the highest-priority alerts where you
                                    // have an associated activity that you would like to launch after the user
                                    // interacts with the notification. Also, if your app targets Android 10
                                    // or higher, you need to request the USE_FULL_SCREEN_INTENT permission in
                                    // order for the platform to invoke this notification.
                                    .setFullScreenIntent(fullScreenPendingIntent, true);

                    Notification incomingCallNotification = notificationBuilder.build();

                    startForeground(1, incomingCallNotification);
                }
                sendMessageToActivity(getApplicationContext(),alarm);
            } else {
                if (intent.getStringExtra("check_play").equals("off")) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
            }
        }

        return START_NOT_STICKY;
    }

    private void sendMessageToActivity(Context context, Alarm alarm) {

        // tạo một luồng mới check liên tục cái activity đã được mở chưa. nếu mở rồi thì gửi cái alarm đến ngay.


        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {

                //Background work here

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                SharedPreferences prefs = getSharedPreferences("OURINFO", MODE_PRIVATE);
                boolean activityIsOn = prefs.getBoolean("active", false);

                Log.e("sss","checkActivityIsOn: "+activityIsOn);

                if (activityIsOn) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //UI Thread work here
                            Intent intent = new Intent("showAlarmCurrent");
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(ALARM_KEY, alarm);
                            intent.putExtra(BUNDLE_EXTRA, bundle);

                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        }
                    });
                }else {
                    sendMessageToActivity(context, alarm);
                }

            }
        });

//        if (checkActivityIsOn()) {
//            Intent intent = new Intent("showAlarmCurrent");
//            Bundle bundle = new Bundle();
//            bundle.putParcelable(ALARM_KEY, alarm);
//            intent.putExtra(BUNDLE_EXTRA, bundle);
//
//            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//        }else {
//            sendMessageToActivity(context,alarm);
//        }
    }
}
