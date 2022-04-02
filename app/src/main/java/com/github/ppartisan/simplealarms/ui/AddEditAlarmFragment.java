package com.github.ppartisan.simplealarms.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.fragment.app.Fragment;

import com.github.ppartisan.simplealarms.R;
import com.github.ppartisan.simplealarms.data.DatabaseHelper;
import com.github.ppartisan.simplealarms.model.Alarm;
import com.github.ppartisan.simplealarms.model.TypeAlarm;
import com.github.ppartisan.simplealarms.service.AlarmReceiver;
import com.github.ppartisan.simplealarms.service.LoadAlarmsService;
import com.github.ppartisan.simplealarms.util.ViewUtils;

import java.util.Calendar;

public final class AddEditAlarmFragment extends Fragment {

    private static final int TYPE_ALARM_MATH = 0;
    private static final int TYPE_ALARM_WRITE = 1;
    private static final int TYPE_ALARM_SCANQR = 2;
    private static final int TYPE_ALARM_WALK = 3;
    private static final int TYPE_ALARM_VIBRATE = 4;
    private TimePicker mTimePicker;
    private EditText mLabel;
    private CheckBox mMon, mTues, mWed, mThurs, mFri, mSat, mSun;
    private TextView tvSave, tvDelete, tvTypeAlarm;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;
    private TypeAlarm typeAlarm;

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

        final Alarm alarm = getAlarm();

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
        tvTypeAlarm = v.findViewById(R.id.tv_type_alarm);

        tvTypeAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogTypeAlarm();
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

        return v;
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
        Toast.makeText(getContext(), data.getStringExtra("result"), Toast.LENGTH_SHORT).show();
//        typeAlarm = new TypeAlarm();
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

        }
        someActivityResultLauncher.launch(intent);
    }

    private void openDialogTypeAlarm() {
        Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.activity_choose_type_alarm);

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

            Toast.makeText(getContext(), messageId, Toast.LENGTH_SHORT).show();

            AlarmReceiver.setReminderAlarm(getContext(), alarm);

            getActivity().finish();
        } else {
            Toast.makeText(getContext(), "Chọn ngày cần báo thức", Toast.LENGTH_SHORT).show();
        }

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
