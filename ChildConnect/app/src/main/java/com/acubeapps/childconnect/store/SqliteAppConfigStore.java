package com.acubeapps.childconnect.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.acubeapps.childconnect.model.AppConfig;
import com.acubeapps.childconnect.model.AppSessionConfig;
import com.acubeapps.childconnect.model.AppStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class SqliteAppConfigStore extends SQLiteOpenHelper {

    private static final String DB_NAME = "appconfig.store";
    private static final String TABLE_NAME = "appconfigtable";
    private static final String ID = "id";
    private static final String PACKAGE_NAME = "packagename";
    private static final String SESSION_START_TIME = "sessionstarttime";
    private static final String SESSION_END_TIME = "sessionendtime";
    private static final String SESSION_ALLOWED_DURATION = "sessionallowedduration";
    private static final String SESSION_STATUS = "sessionstatus";
    private static final String SESSION_TASK_ID = "sessiontaskid";

    public SqliteAppConfigStore(Context context, int version) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CreateAppConfigTable = "CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY,"
                + PACKAGE_NAME + " TEXT,"
                + SESSION_START_TIME + " INTEGER,"
                + SESSION_END_TIME + " INTEGER,"
                + SESSION_ALLOWED_DURATION + " INTEGER,"
                + SESSION_STATUS + " INTEGER,"
                + SESSION_TASK_ID + " TEXT"
                + ")";
        db.execSQL(CreateAppConfigTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public AppConfig getAppConfig(String packageName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
                + PACKAGE_NAME + "='" + packageName + "'" , null);
        List<AppSessionConfig> appSessionConfigList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                long sessionStartTime = cursor.getLong(cursor.getColumnIndex(SESSION_START_TIME));
                long sessionEndTime = cursor.getLong(cursor.getColumnIndex(SESSION_END_TIME));
                long sessionAllowedDuration = cursor.getLong(cursor.getColumnIndex(SESSION_ALLOWED_DURATION));
                int sessionStatus = cursor.getInt(cursor.getColumnIndex(SESSION_STATUS));
                AppStatus appStatus = sessionStatus > 0 ? AppStatus.ALLOWED : AppStatus.BLOCKED;
                String taskId = cursor.getString(cursor.getColumnIndex(SESSION_TASK_ID));
                appSessionConfigList.add(new AppSessionConfig(sessionStartTime, sessionEndTime,
                        sessionAllowedDuration, appStatus, taskId));
            } while (cursor.moveToNext());
        }
        AppConfig appConfig = new AppConfig(packageName, appSessionConfigList);
        return appConfig;
    }

    public void insertOrUpdateAppConfig(AppConfig appConfig) {
        SQLiteDatabase db = this.getWritableDatabase();
        String packageName = appConfig.getAppName();
        deleteAppConfig(packageName);
        List<AppSessionConfig> appSessionConfigList = appConfig.getAppSessionConfigList();
        for (AppSessionConfig appSessionConfig : appSessionConfigList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PACKAGE_NAME, packageName);
            contentValues.put(SESSION_START_TIME, appSessionConfig.getSessionStartTime());
            contentValues.put(SESSION_END_TIME, appSessionConfig.getSessionEndTime());
            contentValues.put(SESSION_ALLOWED_DURATION, appSessionConfig.getSessionAllowedDuration());
            contentValues.put(SESSION_STATUS, (appSessionConfig.getStatus().equals(AppStatus.ALLOWED) ? 1 : -1));
            contentValues.put(SESSION_TASK_ID, appSessionConfig.getTaskId());
            db.insert(TABLE_NAME, null, contentValues);
        }
    }

    public void deleteAppConfig(String packageName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, PACKAGE_NAME + "='" + packageName + "'", null);
    }
}

