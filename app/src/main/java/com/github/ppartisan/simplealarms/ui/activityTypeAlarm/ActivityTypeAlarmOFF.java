package com.github.ppartisan.simplealarms.ui.activityTypeAlarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.ppartisan.simplealarms.R;
import com.github.ppartisan.simplealarms.model.TypeAlarm;

public class ActivityTypeAlarmOFF extends AppCompatActivity {

    private static final String BUNDLE_EXTRA = "bundle_extra";
    private static final String TYPE_ALARM_KEY = "type_alarm_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_alarm_off);

        TypeAlarm typeAlarm = new TypeAlarm("off",0,0,0,null);
        Intent returnIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TYPE_ALARM_KEY, typeAlarm);
        returnIntent.putExtra(BUNDLE_EXTRA, bundle);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}