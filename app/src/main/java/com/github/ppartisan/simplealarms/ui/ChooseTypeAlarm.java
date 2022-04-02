package com.github.ppartisan.simplealarms.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.ppartisan.simplealarms.R;

public class ChooseTypeAlarm extends AppCompatActivity {

    private TextView tvSave, tvDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_type_alarm);

//        tvSave = findViewById(R.id.tv_save);
//        tvDelete = findViewById(R.id.tv_delete);
//
//        tvSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                save();
//            }
//        });
//
//        tvDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cancel();
//            }
//        });

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