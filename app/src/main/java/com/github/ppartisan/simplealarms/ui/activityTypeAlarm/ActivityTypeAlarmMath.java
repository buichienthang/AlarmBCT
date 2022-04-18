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

public class ActivityTypeAlarmMath extends AppCompatActivity {

    TextView tvBack, tvTitleLevelMath, tvTitleLevelMathDemo, tvDelete, tvSave;
    NumberPicker npLevelMath, npTurnMath;
    TypeAlarm typeAlarm;

    private static final String BUNDLE_EXTRA = "bundle_extra";
    private static final String TYPE_ALARM_KEY = "type_alarm_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_alarm_math);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initView();

    }

    private void initView() {
        tvBack = findViewById(R.id.tv_toolbar);
        tvTitleLevelMath = findViewById(R.id.tv_title_level);
        tvTitleLevelMathDemo = findViewById(R.id.tv_title_level_demo);
        tvDelete = findViewById(R.id.tv_delete);
        tvSave = findViewById(R.id.tv_save);
        npLevelMath = findViewById(R.id.np_level_math);
        npTurnMath = findViewById(R.id.np_turn_math);

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeAlarm = new TypeAlarm("math",npLevelMath.getValue(),npTurnMath.getValue(),0,null);
                save(typeAlarm);
            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        setupNumberpicker();
    }

    private void setupNumberpicker() {
        npLevelMath.setMaxValue(5);
        npLevelMath.setMinValue(1);
        npLevelMath.setValue(1);
        npLevelMath.setWrapSelectorWheel(false);

        npTurnMath.setMaxValue(99);
        npTurnMath.setMinValue(1);
        npTurnMath.setValue(1);
        npTurnMath.setWrapSelectorWheel(false);
    }

    private void cancel() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    private void save(TypeAlarm typeAlarm) {
        // đang mắc ở đây
        Intent returnIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TYPE_ALARM_KEY, typeAlarm);
        returnIntent.putExtra(BUNDLE_EXTRA, bundle);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}