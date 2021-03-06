package com.github.ppartisan.simplealarms.ui.activityTypeAlarm;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.ppartisan.simplealarms.R;
import com.github.ppartisan.simplealarms.adapter.AdapterContent;
import com.github.ppartisan.simplealarms.data.DatabaseContentGameWrite;
import com.github.ppartisan.simplealarms.model.ContentWrite;
import com.github.ppartisan.simplealarms.model.TypeAlarm;

import java.util.ArrayList;
import java.util.List;

public class ActivityTypeAlarmWrite extends AppCompatActivity {

    TextView tvBack, tvDelete, tvSave;
    TypeAlarm typeAlarm;
    NumberPicker npTurnWrite;
    RelativeLayout rlWriteAuto, rlWriteEdit;
    CheckBox cbWriteAuto, cbWriteEdit;
    DatabaseContentGameWrite databaseContentGameWrite;

    private static final String BUNDLE_EXTRA = "bundle_extra";
    private static final String TYPE_ALARM_KEY = "type_alarm_key";
    private Dialog dialogEditContent, dialogAddContent;
    private AdapterContent adapterContent;
    private ListView lvContentWrite;
    private List<ContentWrite> listContent;
    private Dialog dialogDeleteContent;
    private String CONTENT_WRITE = "contentwrite";
    private String NAME_TABLE_SQL = "contentwrite.sqlite";
    private boolean checkStartSetupRecent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_alarm_write);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        typeAlarm = getIntent().getBundleExtra(BUNDLE_EXTRA).getParcelable(TYPE_ALARM_KEY);

        initView();

        if (typeAlarm.getTypeTurnOffAlarm().equals("write")) {
            setUpSelectRecent(typeAlarm);
        }
    }

    private void setUpSelectRecent(TypeAlarm typeAlarm) {
        checkStartSetupRecent = true;
        String typeWriteAuto = getResources().getString(R.string.type_write_auto);
        String typeWriteEdit = getResources().getString(R.string.type_write_edit);
        if (typeAlarm.getTypeWrite().equals(typeWriteAuto)) {
            rlWriteAuto.setBackgroundResource(R.drawable.boder_circle_1dp_accent);
            cbWriteAuto.setChecked(true);
        } else {
            if (typeAlarm.getTypeWrite().equals(typeWriteEdit)) {
                rlWriteEdit.setBackgroundResource(R.drawable.boder_circle_1dp_accent);
                cbWriteEdit.setChecked(true);
            }
        }
        npTurnWrite.setValue(typeAlarm.getTimes());
        checkStartSetupRecent = false;
    }

    private void setUplv() {
        listContent = getListContentWrite();
        adapterContent = new AdapterContent(this, R.layout.item_content_write, listContent);
        lvContentWrite.setAdapter(adapterContent);

        lvContentWrite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openDialogUpdateContent(listContent.get(i).getId(), listContent.get(i).getContent());
            }
        });

        lvContentWrite.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialogRemoveContent(i);
                return false;
            }
        });
    }

    private void openDialogUpdateContent(int id, String content) {
        dialogAddContent = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialogAddContent.setContentView(R.layout.layuot_add_content);

        EditText etNewContent = dialogAddContent.findViewById(R.id.et_add_content);
        etNewContent.setText(content);

        TextView tvSaveContent = dialogAddContent.findViewById(R.id.tv_save_content);
        tvSaveContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateContent(id, etNewContent.getText().toString());
            }
        });

        dialogAddContent.show();
    }

    private void updateContent(int id, String content) {

        String sqlUpdate = "UPDATE " + CONTENT_WRITE + " SET id = " + id + ", content = '" + content + "' WHERE id=" + id + ";";

        databaseContentGameWrite.queryData(sqlUpdate);

        listContent.clear();

        listContent.addAll(getListContentWrite());

        adapterContent.notifyDataSetChanged();

        dialogAddContent.dismiss();
    }

    private void dialogRemoveContent(int i) {

        dialogDeleteContent = new Dialog(this);
        dialogDeleteContent.setContentView(R.layout.dialog_delete_content);
        TextView tvDelete = dialogDeleteContent.findViewById(R.id.tv_delete);
        TextView tvCancel = dialogDeleteContent.findViewById(R.id.tv_cancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDeleteContent.dismiss();
            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int id = listContent.get(i).getId();

                listContent.remove(i);
                adapterContent.notifyDataSetChanged();
                dialogDeleteContent.dismiss();
                String sql = "DELETE FROM " + CONTENT_WRITE + " WHERE id=" + id + ";";
                databaseContentGameWrite.queryData(sql);
            }
        });

        dialogDeleteContent.show();
    }

    private List<ContentWrite> getListContentWrite() {
        databaseContentGameWrite = new DatabaseContentGameWrite(getApplicationContext(), NAME_TABLE_SQL, null, 1);

        List<ContentWrite> list = new ArrayList<>();

        // ??ang code d??? c??i lvQrcode ??? ????y.
        Cursor data = databaseContentGameWrite.getData("SELECT * FROM " + CONTENT_WRITE + ";");

        if (data != null) {
            if (data.moveToFirst()) {
                do {
                    // on below line we are adding the data from cursor to our array list.
                    list.add(new ContentWrite(data.getInt(0), data.getString(1)));
                } while (data.moveToNext());
                // moving our cursor to next.
            }
            // at last closing our cursor
            // and returning our array list.
            data.close();
        }

        return list;
    }

    private void initView() {
        tvBack = findViewById(R.id.tv_toolbar);
        tvDelete = findViewById(R.id.tv_delete);
        tvSave = findViewById(R.id.tv_save);
        npTurnWrite = findViewById(R.id.np_turn_write);
        rlWriteAuto = findViewById(R.id.rl_write_auto);
        rlWriteEdit = findViewById(R.id.rl_write_edit);
        cbWriteAuto = findViewById(R.id.cb_write_auto);
        cbWriteEdit = findViewById(R.id.cb_write_edit);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        cbWriteAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    cbWriteAuto.setChecked(true);
                    cbWriteEdit.setChecked(false);
                    rlWriteAuto.setBackgroundResource(R.drawable.boder_circle_1dp_accent);
                    rlWriteEdit.setBackgroundResource(R.drawable.boder_gray80);
                }
            }
        });

        cbWriteEdit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    cbWriteEdit.setChecked(true);
                    cbWriteAuto.setChecked(false);
                    rlWriteEdit.setBackgroundResource(R.drawable.boder_circle_1dp_accent);
                    rlWriteAuto.setBackgroundResource(R.drawable.boder_gray80);

                    if (!checkStartSetupRecent) {
                        showDialogContentEdit();
                    }
                }
            }
        });


        rlWriteAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cbWriteAuto.isChecked()) {
                    cbWriteAuto.setChecked(true);
                    cbWriteEdit.setChecked(false);
                }
            }
        });

        rlWriteEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cbWriteEdit.isChecked()) {
                    cbWriteEdit.setChecked(true);
                    cbWriteAuto.setChecked(false);

                    showDialogContentEdit();
                }
            }
        });

        setupNumberPicker();

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeAlarm = new TypeAlarm("write", 0, npTurnWrite.getValue(), 0, getTypeWrite());
                save(typeAlarm);
            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        databaseContentGameWrite = new DatabaseContentGameWrite(this, NAME_TABLE_SQL, null, 1);

        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + "" + CONTENT_WRITE + "" + " (" +
                "id" + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "content" + " TEXT" +
                ");";
        databaseContentGameWrite.queryData(sqlCreate);
    }

    private void showDialogContentEdit() {

        dialogEditContent = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialogEditContent.setContentView(R.layout.layuot_lv_content_edit);

        lvContentWrite = dialogEditContent.findViewById(R.id.lv_content_edit);
        setUplv();

        RelativeLayout rlBack = dialogEditContent.findViewById(R.id.rl_toolbar);
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogEditContent.dismiss();
            }
        });

        TextView tvAddContent = dialogEditContent.findViewById(R.id.tv_add_content);
        tvAddContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddContent();
            }
        });

        dialogEditContent.show();

    }

    private void showDialogAddContent() {
        dialogAddContent = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialogAddContent.setContentView(R.layout.layuot_add_content);

        EditText etNewContent = dialogAddContent.findViewById(R.id.et_add_content);
        TextView tvSaveContent = dialogAddContent.findViewById(R.id.tv_save_content);
        tvSaveContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveContentNew(etNewContent.getText().toString());
            }
        });

        dialogAddContent.show();
    }

    private void saveContentNew(String toString) {
        //
        String sqlInsert = "INSERT INTO " + "" + CONTENT_WRITE + "" + " VALUES (" + " null" +
                " , " +
                "'" + toString + "'" +
                ");";
        databaseContentGameWrite.queryData(sqlInsert);

        listContent.clear();

        listContent.addAll(getListContentWrite());

        adapterContent.notifyDataSetChanged();

        dialogAddContent.dismiss();
    }

    private String getTypeWrite() {
        String typeWrite = "";
        if (cbWriteAuto.isChecked()) {
            typeWrite = getString(R.string.type_write_auto);
        }
        if (cbWriteEdit.isChecked()) {
            typeWrite = getString(R.string.type_write_edit);
        }
        return typeWrite;
    }

    private void setupNumberPicker() {
        npTurnWrite.setMaxValue(99);
        npTurnWrite.setMinValue(1);
        npTurnWrite.setValue(1);
        npTurnWrite.setWrapSelectorWheel(false);
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