package com.github.ppartisan.simplealarms.ui.activityTypeAlarm;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.ppartisan.simplealarms.R;
import com.github.ppartisan.simplealarms.adapter.AdapterQrCode;
import com.github.ppartisan.simplealarms.data.DatabaseQrcode;
import com.github.ppartisan.simplealarms.model.Qrcode;
import com.github.ppartisan.simplealarms.model.TypeAlarm;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class ActivityTypeAlarmScanQr extends AppCompatActivity {

    private static final String NAME_TABLE_SQL = "qrcode.sqlite";
    private static final String QR_CODE = "qrcode";
    private static final String TYPE_ALARM_SCANQR_STRING = "scanqr";
    TextView tvBack, tvAddQrcode;
    ListView lvQrCode;
    AdapterQrCode adapterQrCode;

    private static final String BUNDLE_EXTRA = "bundle_extra";
    private static final String TYPE_ALARM_KEY = "type_alarm_key";
    private DatabaseQrcode dataBase;

    List<Qrcode> listQrcode;
    private Dialog dialogAddNameQrcode;
    private Dialog dialogDeleteQrcode;
    private TypeAlarm typeAlarm;
    private  int idQrRecent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_alarm_scan_qr);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        typeAlarm = getIntent().getBundleExtra(BUNDLE_EXTRA).getParcelable(TYPE_ALARM_KEY);

        if (typeAlarm.getTypeTurnOffAlarm().equals("scanqr")) {
            setUpSelectRecent(typeAlarm);
        }

        initView();
    }

    private void setUpSelectRecent(TypeAlarm typeAlarm) {
        idQrRecent = typeAlarm.getIdQrcode();
    }

    private void initView() {

        createDB();

        tvBack = findViewById(R.id.tv_toolbar);
        lvQrCode = findViewById(R.id.lv_qrcode);
        tvAddQrcode = findViewById(R.id.tv_add_content);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        tvAddQrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(ActivityTypeAlarmScanQr.this);
                scanIntegrator.setPrompt("Scan");
                scanIntegrator.setBeepEnabled(true);
                //The following line if you want QR code
                scanIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                scanIntegrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
                scanIntegrator.setOrientationLocked(true);
                scanIntegrator.setBarcodeImageEnabled(true);
                scanIntegrator.initiateScan();
            }
        });

        listQrcode = getListDataQrcode();
        adapterQrCode = new AdapterQrCode(this, R.layout.item_listview_qrcode, listQrcode, idQrRecent);
        lvQrCode.setAdapter(adapterQrCode);

        lvQrCode.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialogRemoveQrcode(i);
                return false;
            }
        });

        lvQrCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TypeAlarm typeAlarm = new TypeAlarm(TYPE_ALARM_SCANQR_STRING,0,0,
                        listQrcode.get(i).getId(),null);
                save(typeAlarm);
            }
        });

    }

    private void dialogRemoveQrcode(int i) {
        dialogDeleteQrcode = new Dialog(this);
        dialogDeleteQrcode.setContentView(R.layout.dialog_delete_content);
        TextView tvDelete = dialogDeleteQrcode.findViewById(R.id.tv_delete);
        TextView tvCancel = dialogDeleteQrcode.findViewById(R.id.tv_cancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDeleteQrcode.dismiss();
            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int id = listQrcode.get(i).getId();

                listQrcode.remove(i);
                adapterQrCode.notifyDataSetChanged();
                dialogDeleteQrcode.dismiss();
                String sql = "DELETE FROM " + QR_CODE + " WHERE id=" + id + ";";
                dataBase.queryData(sql);
            }
        });

        dialogDeleteQrcode.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null) {
            String scanContent = null, scanFormat = null;
            if (scanningResult.getContents() != null) {
                scanContent = scanningResult.getContents().toString();
                scanFormat = scanningResult.getFormatName().toString();
            }

            openDialogAddNameQrcode(scanContent);

        } else {
            Toast.makeText(this, "Nothing scanned", Toast.LENGTH_SHORT).show();
        }
    }

    private void openDialogAddNameQrcode(String scanContent) {

        dialogAddNameQrcode = new Dialog(this);
        dialogAddNameQrcode.setContentView(R.layout.dialog_add_name_qrcode);

        EditText etNameQrcode = dialogAddNameQrcode.findViewById(R.id.et_add_name_qrcode);

        TextView tvSave = dialogAddNameQrcode.findViewById(R.id.tv_save);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameQrcode = etNameQrcode.getText().toString();
                saveQrcode(nameQrcode,scanContent);
                dialogAddNameQrcode.dismiss();
            }
        });

        TextView tvCancel = dialogAddNameQrcode.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddNameQrcode.dismiss();
            }
        });

        dialogAddNameQrcode.show();

    }

    private void saveQrcode(String nameQrcode,String scanContent) {
        String sqlInsert = "INSERT INTO " + "" + QR_CODE + "" + " VALUES (" + " null ," +
                "'" + nameQrcode + "'," +
                "'" + scanContent + "'" +
                ");";
        dataBase.queryData(sqlInsert);

        listQrcode.clear();

        listQrcode.addAll(getListDataQrcode());

        adapterQrCode.notifyDataSetChanged();
    }

    private void createDB() {
        dataBase = new DatabaseQrcode(this, NAME_TABLE_SQL, null, 1);

        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + "" + QR_CODE + "" + " (" +
                "id" + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nameqr" + " TEXT," +
                "qrtotext" + " TEXT" +
                ");";
        dataBase.queryData(sqlCreate);
    }

    private List<Qrcode> getListDataQrcode() {

        dataBase = new DatabaseQrcode(getApplicationContext(), NAME_TABLE_SQL, null, 1);

        List<Qrcode> list = new ArrayList<>();

        // đang code dở cái lvQrcode ở đây.
        Cursor data = dataBase.getData("SELECT * FROM " + QR_CODE + ";");

        if (data != null) {
            if (data.moveToFirst()) {
                do {
                    // on below line we are adding the data from cursor to our array list.
                    list.add(new Qrcode(data.getInt(0), data.getString(1), data.getString(2)));
                } while (data.moveToNext());
                // moving our cursor to next.
            }
            // at last closing our cursor
            // and returning our array list.
            data.close();
        }

        return list;
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