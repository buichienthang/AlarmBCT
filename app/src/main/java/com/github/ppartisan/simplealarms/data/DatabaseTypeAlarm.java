package com.github.ppartisan.simplealarms.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseTypeAlarm extends SQLiteOpenHelper {

    private static DatabaseTypeAlarm sInstance = null;
    private static final String DATABASE_NAME = "typealarms.db";
    private static final int SCHEMA = 2;

    private static final String TABLE_NAME = "type_alarm";
    public static final String _ID = "_id";
    public static final String COL_TYPE_ALARM = "type_alarm";
    public static final String COL_TURN = "turn";
    public static final String COL_LEVEL = "level";
    public static final String COL_AUTO_EDIT = "auto_edit";
    public static final String COL_CONTENT_TYPE_WRITE = "content_type_write";
    public static final String COL_NAME_QR = "name_qr";
    public static final String COL_QR_TO_TEXT = "qr_to_text";

    public DatabaseTypeAlarm(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static synchronized DatabaseTypeAlarm getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseTypeAlarm(context.getApplicationContext());
        }
        return sInstance;
    }

    private DatabaseTypeAlarm(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

//        final String CREATE_TYPE_ALARMS_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
//                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                COL_TYPE_ALARM + " TEXT, " +
//                COL_TURN + " INTEGER , " +
//                COL_LEVEL + " INTEGER , " +
//                COL_AUTO_EDIT + " TEXT, " +
//                COL_CONTENT_TYPE_WRITE + " TEXT, " +
//                COL_NAME_QR + " TEXT, " +
//                COL_QR_TO_TEXT + " TEXT" +
//                ");";
//
//        sqLiteDatabase.execSQL(CREATE_TYPE_ALARMS_TABLE);

        Log.e("sss", "Creating database...");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        throw new UnsupportedOperationException("This shouldn't happen yet!");
    }

    public void queryData(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
        Log.e("sss","add type alarm xong !");
    }

    public Cursor getData(String sql) {
        SQLiteDatabase database = getReadableDatabase();
        database.execSQL(sql);
        return database.rawQuery(sql, null);
    }
//
//    public void addTypeAlarm(TypeAlarm typeAlarm, long id) {
//        String sql = "INSERT INTO " + TABLE_NAME + "VALUES (" +
//                id + "," +
//                typeAlarm.getTypeTurnOffAlarm() + "," +
//                typeAlarm.getTimes() + "," +
//                typeAlarm.getLevel() + "," +
//                typeAlarm.getTypeWrite() + "," +
//                typeAlarm.getNameQr() + "," +
//                typeAlarm.getQrToText() +
//                ");";
//        queryData(sql);
//        Log.e("xxx", "save data");
//    }
//
//    public void updateTypeAlarm() {
//
//    }

}
