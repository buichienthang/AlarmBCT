package com.github.ppartisan.simplealarms.ui.activityTypeAlarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.ppartisan.simplealarms.R;
import com.github.ppartisan.simplealarms.model.TypeAlarm;

public class ActivityTypeAlarmWalk extends AppCompatActivity {

    TextView tvBack, tvDelete, tvSave;
    TypeAlarm typeAlarm;
    NumberPicker npWalk;

    private static final String BUNDLE_EXTRA = "bundle_extra";
    private static final String TYPE_ALARM_KEY = "type_alarm_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_alarm_walk);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initView();
    }

    private void initView() {
        tvBack = findViewById(R.id.tv_toolbar);
        tvDelete = findViewById(R.id.tv_delete);
        tvSave = findViewById(R.id.tv_save);
        npWalk = findViewById(R.id.np_turn_walk);

        setupNumberpicker();

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeAlarm = new TypeAlarm("walk",0,npWalk.getValue(),0,null);
                save(typeAlarm);
            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
    }

    private void setupNumberpicker() {

        npWalk.setMaxValue(299);
        npWalk.setMinValue(10);
        npWalk.setValue(20);
        npWalk.setWrapSelectorWheel(false);

    }

    private void cancel() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    private void save(TypeAlarm typeAlarm) {
        Intent returnIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TYPE_ALARM_KEY, typeAlarm);
        returnIntent.putExtra(BUNDLE_EXTRA, bundle);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}