package com.github.ppartisan.simplealarms.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.ppartisan.simplealarms.R;
import com.github.ppartisan.simplealarms.data.DatabaseHelper;
import com.github.ppartisan.simplealarms.data.DatabaseTypeAlarm;
import com.github.ppartisan.simplealarms.model.Alarm;
import com.github.ppartisan.simplealarms.model.TypeAlarm;
import com.github.ppartisan.simplealarms.service.AlarmReceiver;
import com.github.ppartisan.simplealarms.service.LoadAlarmsService;
import com.github.ppartisan.simplealarms.ui.activityTypeAlarm.ActivityTypeAlarmMath;
import com.github.ppartisan.simplealarms.ui.activityTypeAlarm.ActivityTypeAlarmOFF;
import com.github.ppartisan.simplealarms.ui.activityTypeAlarm.ActivityTypeAlarmScanQr;
import com.github.ppartisan.simplealarms.ui.activityTypeAlarm.ActivityTypeAlarmVibrate;
import com.github.ppartisan.simplealarms.ui.activityTypeAlarm.ActivityTypeAlarmWalk;
import com.github.ppartisan.simplealarms.ui.activityTypeAlarm.ActivityTypeAlarmWrite;
import com.github.ppartisan.simplealarms.util.ViewUtils;

import java.util.Calendar;

public final class AddEditAlarmFragment extends Fragment {

    private static final String TYPE_ALARM_MATH_STRING = "math";
    private static final String TYPE_ALARM_WRITE_STRING = "write";
    private static final String TYPE_ALARM_SCANQR_STRING = "scanqr";
    private static final String TYPE_ALARM_WALK_STRING = "walk";
    private static final String TYPE_ALARM_VIBRATE_STRING = "vibrate";
    private static final String TYPE_ALARM_OFF_STRING = "off";
    private static final String TABLE_NAME = "type_alarm";
    String _ID = "_id";
    String COL_TYPE_ALARM = "type_alarm";
    String COL_TURN = "turn";
    String COL_LEVEL = "level";
    String COL_AUTO_EDIT = "auto_edit";
    String COL_ID_QR = "id_qr";

    private static final int TYPE_ALARM_MATH = 0;
    private static final int TYPE_ALARM_WRITE = 1;
    private static final int TYPE_ALARM_SCANQR = 2;
    private static final int TYPE_ALARM_WALK = 3;
    private static final int TYPE_ALARM_VIBRATE = 4;
    private static final int TYPE_ALARM_OFF = 5;
    private TimePicker mTimePicker;
    private EditText mLabel;
    private CheckBox mMon, mTues, mWed, mThurs, mFri, mSat, mSun;
    private TextView tvSave, tvDelete, tvGame;
    private RelativeLayout rlTypeAlarm;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;
    private TypeAlarm typeAlarm;
    private DatabaseTypeAlarm dataBase;

    private static final String BUNDLE_EXTRA = "bundle_extra";
    private static final String TYPE_ALARM_KEY = "type_alarm_key";
    private Dialog dialog;


    public static Fragment newInstance(Alarm alarm) {

        Bundle args = new Bundle();
        args.putParcelable(AddEditAlarmActivity.ALARM_EXTRA, alarm);

        AddEditAlarmFragment fragment = new AddEditAlarmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_add_edit_alarm, container, false);

//        setHasOptionsMenu(true);

        createDB();

        final Alarm alarm = getAlarm();

        typeAlarm = getTypeAlarm(alarm.getId());

        mTimePicker = (TimePicker) v.findViewById(R.id.edit_alarm_time_picker);
        ViewUtils.setTimePickerTime(mTimePicker, alarm.getTime());

        mLabel = (EditText) v.findViewById(R.id.edit_alarm_label);
        if (alarm.getLabel() != null) {
            mLabel.setText(alarm.getLabel());
        } else {
            mLabel.setText(getString(R.string.add_label_hint));
        }

        mMon = (CheckBox) v.findViewById(R.id.edit_alarm_mon);
        mTues = (CheckBox) v.findViewById(R.id.edit_alarm_tues);
        mWed = (CheckBox) v.findViewById(R.id.edit_alarm_wed);
        mThurs = (CheckBox) v.findViewById(R.id.edit_alarm_thurs);
        mFri = (CheckBox) v.findViewById(R.id.edit_alarm_fri);
        mSat = (CheckBox) v.findViewById(R.id.edit_alarm_sat);
        mSun = (CheckBox) v.findViewById(R.id.edit_alarm_sun);
        tvSave = v.findViewById(R.id.tv_save);
        tvDelete = v.findViewById(R.id.tv_delete);
        rlTypeAlarm = v.findViewById(R.id.rl_type_alarm);
        tvGame = v.findViewById(R.id.tv_game);

        rlTypeAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogTypeAlarm(typeAlarm);
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });

        setUpResultTypeAlarm();

        setDayCheckboxes(alarm);

        setTypeAlarm(typeAlarm);

        return v;
    }

    private void setTypeAlarm(TypeAlarm typeAlarm) {
        switch (typeAlarm.getTypeTurnOffAlarm()) {
            case TYPE_ALARM_MATH_STRING:
                tvGame.setText(typeAlarm.getTypeTurnOffAlarm());
                tvGame.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_icmath, 0, 0, 0);
                break;
            case TYPE_ALARM_WRITE_STRING:
                tvGame.setText(typeAlarm.getTypeTurnOffAlarm());
                tvGame.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_icwrite, 0, 0, 0);
                break;
            case TYPE_ALARM_SCANQR_STRING:
                tvGame.setText(typeAlarm.getTypeTurnOffAlarm());
                tvGame.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_qrcode, 0, 0, 0);
                break;
            case TYPE_ALARM_WALK_STRING:
                tvGame.setText(typeAlarm.getTypeTurnOffAlarm());
                tvGame.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_walk, 0, 0, 0);
                break;
            case TYPE_ALARM_VIBRATE_STRING:
                tvGame.setText(typeAlarm.getTypeTurnOffAlarm());
                tvGame.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_vibrate, 0, 0, 0);
                break;
            case TYPE_ALARM_OFF_STRING:
                tvGame.setText(typeAlarm.getTypeTurnOffAlarm());
                tvGame.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_alarm, 0, 0, 0);
                break;
            default:
                tvGame.setText(typeAlarm.getTypeTurnOffAlarm());
                tvGame.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_alarm, 0, 0, 0);
                break;
        }
    }

    private void createDB() {

        final String CREATE_TYPE_ALARMS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY , " +
                COL_TYPE_ALARM + " TEXT, " +
                COL_LEVEL + " INTEGER , " +
                COL_TURN + " INTEGER , " +
                COL_ID_QR + " INTEGER, " +
                COL_AUTO_EDIT + " TEXT" +
                ");";

        dataBase = new DatabaseTypeAlarm(getContext(), "typealarm.sqlite", null, 1);

        dataBase.queryData(CREATE_TYPE_ALARMS_TABLE);
    }

    private TypeAlarm getTypeAlarm(long id) {
        TypeAlarm typeAlarm = null;

        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE _id = " + id + " ;";

        Cursor cursor = dataBase.getData(sql);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    // on below line we are adding the data from cursor to our array list.
                    typeAlarm = new TypeAlarm(cursor.getString(1),
                            cursor.getInt(2),
                            cursor.getInt(3),
                            cursor.getInt(4),
                            cursor.getString(5));
                } while (cursor.moveToNext());
                // moving our cursor to next.
            }
            // at last closing our cursor
            // and returning our array list.
            cursor.close();
        }

        if (typeAlarm != null) {
            return typeAlarm;
        } else {
            return new TypeAlarm("off", 0, 0, 0, null);
        }
    }

    private void setUpResultTypeAlarm() {

        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            doSomeOperations(data);
                        }
                    }
                });
    }

    private void doSomeOperations(Intent data) {

        typeAlarm = data.getBundleExtra(BUNDLE_EXTRA).getParcelable(TYPE_ALARM_KEY);

        setTypeAlarm(typeAlarm);

        dialog.dismiss();
    }

    private void openActivityChooseTypeAlarm(int intAlarmType) {
        Intent intent = null;
        switch (intAlarmType) {
            case TYPE_ALARM_MATH:
                intent = new Intent(getContext(), ActivityTypeAlarmMath.class);
                break;
            case TYPE_ALARM_WRITE:
                intent = new Intent(getContext(), ActivityTypeAlarmWrite.class);
                break;
            case TYPE_ALARM_SCANQR:
                intent = new Intent(getContext(), ActivityTypeAlarmScanQr.class);
                break;
            case TYPE_ALARM_WALK:
                intent = new Intent(getContext(), ActivityTypeAlarmWalk.class);
                break;
            case TYPE_ALARM_VIBRATE:
                intent = new Intent(getContext(), ActivityTypeAlarmVibrate.class);
                break;
            case TYPE_ALARM_OFF:
                intent = new Intent(getContext(), ActivityTypeAlarmOFF.class);
                break;

        }

        Bundle bundle = new Bundle();
        bundle.putParcelable(TYPE_ALARM_KEY, typeAlarm);
        intent.putExtra(BUNDLE_EXTRA, bundle);

        someActivityResultLauncher.launch(intent);
    }

    private void openDialogTypeAlarm(TypeAlarm typeAlarm) {
        dialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_choose_type_alarm);

        RelativeLayout rlOut = dialog.findViewById(R.id.rl_back);
        rlOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        LinearLayout llMath = dialog.findViewById(R.id.ll_math);
        llMath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityChooseTypeAlarm(TYPE_ALARM_MATH);
            }
        });

        LinearLayout llWrite = dialog.findViewById(R.id.ll_write);
        llWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityChooseTypeAlarm(TYPE_ALARM_WRITE);
            }
        });

        LinearLayout llScanQrCode = dialog.findViewById(R.id.ll_scan_qr);
        llScanQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityChooseTypeAlarm(TYPE_ALARM_SCANQR);
            }
        });

        LinearLayout llWalk = dialog.findViewById(R.id.ll_walk);
        llWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityChooseTypeAlarm(TYPE_ALARM_WALK);
            }
        });

        LinearLayout llVibrate = dialog.findViewById(R.id.ll_vibrate);
        llVibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityChooseTypeAlarm(TYPE_ALARM_VIBRATE);
            }
        });

        LinearLayout llOff = dialog.findViewById(R.id.ll_off);
        llOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityChooseTypeAlarm(TYPE_ALARM_OFF);
            }
        });

        switch (typeAlarm.getTypeTurnOffAlarm()) {
            case TYPE_ALARM_MATH_STRING:
                llMath.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.boder_circle_1dp_accent));
                break;
            case TYPE_ALARM_WRITE_STRING:
                llWrite.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.boder_circle_1dp_accent));
                break;
            case TYPE_ALARM_SCANQR_STRING:
                llScanQrCode.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.boder_circle_1dp_accent));
                break;
            case TYPE_ALARM_WALK_STRING:
                llWalk.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.boder_circle_1dp_accent));
                break;
            case TYPE_ALARM_VIBRATE_STRING:
                llVibrate.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.boder_circle_1dp_accent));
                break;
            case TYPE_ALARM_OFF_STRING:
                llOff.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.boder_circle_1dp_accent));
                break;
            default:
                llOff.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.boder_circle_1dp_accent));
                break;
        }

        dialog.show();
    }

    private Alarm getAlarm() {
        return getArguments().getParcelable(AddEditAlarmActivity.ALARM_EXTRA);
    }

    private void setDayCheckboxes(Alarm alarm) {
        mMon.setChecked(alarm.getDay(Alarm.MON));
        mTues.setChecked(alarm.getDay(Alarm.TUES));
        mWed.setChecked(alarm.getDay(Alarm.WED));
        mThurs.setChecked(alarm.getDay(Alarm.THURS));
        mFri.setChecked(alarm.getDay(Alarm.FRI));
        mSat.setChecked(alarm.getDay(Alarm.SAT));
        mSun.setChecked(alarm.getDay(Alarm.SUN));
    }

    private void save() {
        if (mMon.isChecked() || mTues.isChecked() || mWed.isChecked() || mThurs.isChecked() || mFri.isChecked()
                || mSat.isChecked() || mSun.isChecked()) {
            final Alarm alarm = getAlarm();

            final Calendar time = Calendar.getInstance();
            time.set(Calendar.MINUTE, ViewUtils.getTimePickerMinute(mTimePicker));
            time.set(Calendar.HOUR_OF_DAY, ViewUtils.getTimePickerHour(mTimePicker));
            alarm.setTime(time.getTimeInMillis());

            alarm.setLabel(mLabel.getText().toString());

            alarm.setDay(Alarm.MON, mMon.isChecked());
            alarm.setDay(Alarm.TUES, mTues.isChecked());
            alarm.setDay(Alarm.WED, mWed.isChecked());
            alarm.setDay(Alarm.THURS, mThurs.isChecked());
            alarm.setDay(Alarm.FRI, mFri.isChecked());
            alarm.setDay(Alarm.SAT, mSat.isChecked());
            alarm.setDay(Alarm.SUN, mSun.isChecked());
            alarm.setIsEnabled(true);

            final int rowsUpdated = DatabaseHelper.getInstance(getContext()).updateAlarm(alarm);
            final int messageId = (rowsUpdated == 1) ? R.string.update_complete : R.string.update_failed;

            // save typeAlarm
            saveTypeAlarm(typeAlarm, alarm.getId());

            Toast.makeText(getContext(), messageId, Toast.LENGTH_SHORT).show();

            AlarmReceiver.setReminderAlarm(getContext(), alarm);

            getActivity().finish();
        } else {
            Toast.makeText(getContext(), "Chọn ngày cần báo thức", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveTypeAlarm(TypeAlarm typeAlarm, long id) {

//        INSERT INTO type_alarm(_id, type_alarm, level, turn, id_qr, auto_edit)
//        SELECT 3, 'math', 4, 4, '0', 'null' WHERE NOT
//        EXISTS(SELECT 1 FROM type_alarm WHERE _id = 3);
//
//        String sql = "INSERT INTO " + TABLE_NAME + " VALUES (" +
//                id + "," +
//                "'" + typeAlarm.getTypeTurnOffAlarm() + "'" + "," +
//                typeAlarm.getLevel() + "," +
//                typeAlarm.getTimes() + "," +
//                "'" + typeAlarm.getIdQrcode() + "'" + "," +
//                "'" + typeAlarm.getTypeWrite() + "'" +
//                ") WHERE NOT EXISTS(SELECT 1 FROM " + TABLE_NAME + " WHERE id = " + id + ");";

        String sql = "INSERT INTO " + TABLE_NAME + "(_id, type_alarm, level, turn,id_qr, auto_edit) " +
                "SELECT "+id+",'"+typeAlarm.getTypeTurnOffAlarm()+"',"+typeAlarm.getLevel()+
                ","+typeAlarm.getTimes()+",'"+typeAlarm.getIdQrcode()+"','"+typeAlarm.getTypeWrite()+
                "' WHERE NOT EXISTS(SELECT 1 FROM "+TABLE_NAME+" WHERE _id = "+id+");";

        dataBase.queryData(sql);

        sql = "UPDATE " + TABLE_NAME + " SET " + COL_TYPE_ALARM + " = '" + typeAlarm.getTypeTurnOffAlarm() + "'," +
                COL_LEVEL + "= '" + typeAlarm.getLevel() + "', " +
                COL_TURN + "= '" + typeAlarm.getTimes() + "', " +
                COL_ID_QR + "= '" + typeAlarm.getIdQrcode() + "', " +
                COL_AUTO_EDIT + "= '" + typeAlarm.getTypeWrite() + "' " +
                "WHERE _id = " + id + ";";

        dataBase.queryData(sql);

//        dataBase.addTypeAlarm(typeAlarm, id);
    }

    private void delete() {

        final Alarm alarm = getAlarm();

        final AlertDialog.Builder builder =
                new AlertDialog.Builder(getContext(), R.style.DeleteAlarmDialogTheme);
        builder.setTitle(R.string.delete_dialog_title);
        builder.setMessage(R.string.delete_dialog_content);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //Cancel any pending notifications for this alarm
                AlarmReceiver.cancelReminderAlarm(getContext(), alarm);

                final int rowsDeleted = DatabaseHelper.getInstance(getContext()).deleteAlarm(alarm);
                int messageId;
                if (rowsDeleted == 1) {
                    messageId = R.string.delete_complete;
                    Toast.makeText(getContext(), messageId, Toast.LENGTH_SHORT).show();
                    LoadAlarmsService.launchLoadAlarmsService(getContext());
                    getActivity().finish();
                } else {
                    messageId = R.string.delete_failed;
                    Toast.makeText(getContext(), messageId, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.no, null);
        builder.show();

    }

}
