package com.github.ppartisan.simplealarms.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.github.ppartisan.simplealarms.R;
import com.github.ppartisan.simplealarms.model.Alarm;
import com.github.ppartisan.simplealarms.service.MusicService;
import com.github.ppartisan.simplealarms.ui.fragmentGame.FragmentMath;

public class showAlarmActivity extends AppCompatActivity {

    RelativeLayout relativeLayout;
    private static final String BUNDLE_EXTRA = "bundle_extra";
    private static final String ALARM_KEY = "alarm_key";
    private Alarm alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_alarm2);

        relativeLayout = findViewById(R.id.rl_cancel_alarm);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // tat am thanh
                stopMusic(getApplicationContext());
                relativeLayout.setVisibility(View.GONE);
            }
        });
    }

    private void repalaceFragement(Fragment fragment) {
        FragmentManager fragmentManager  = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout_game,fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        unlockScreen();
        super.onResume();
    }

    private void unlockScreen() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    private void stopMusic(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent(context, MusicService.class);
            intent.putExtra("check_play","off");
            ContextCompat.startForegroundService(this, intent);
        } else {
            Intent intent = new Intent(context, MusicService.class);
            intent.putExtra("check_play","off");
            context.startService(intent);
        }

        // hiện cái rlgame lên.
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            alarm = intent.getBundleExtra(BUNDLE_EXTRA).getParcelable(ALARM_KEY);
//            switch (alarm.get)
            // check alarm và gán fragment
            repalaceFragement(new FragmentMath());
        }
    };

    @Override
    protected void onStart() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("showAlarmCurrent"));

        SharedPreferences sp = getSharedPreferences("OURINFO", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("active", true);
        ed.commit();

        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Store our shared preference
        SharedPreferences sp = getSharedPreferences("OURINFO", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("active", false);
        ed.commit();

    }
}