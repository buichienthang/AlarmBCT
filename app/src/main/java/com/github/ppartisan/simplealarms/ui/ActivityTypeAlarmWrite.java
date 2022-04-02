package com.github.ppartisan.simplealarms.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.ppartisan.simplealarms.R;

public class ActivityTypeAlarmWrite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_alarm_write);
    }

    private void cancel() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    private void save() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result","sss");
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}