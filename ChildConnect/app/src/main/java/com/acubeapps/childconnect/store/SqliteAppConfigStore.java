package com.acubeapps.childconnect.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.acubeapps.childconnect.model.AppConfig;
import com.acubeapps.childconnect.model.AppSessionConfig;
import com.acubeapps.childconnect.model.AppStatus;
import com.acubeapps.childconnect.model.LocalCourse;
import com.acubeapps.childconnect.model.McqOptions;
import com.acubeapps.childconnect.model.QuestionDetails;
import com.acubeapps.childconnect.model.QuestionType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class SqliteAppConfigStore extends SQLiteOpenHelper {

    private static final String DB_NAME = "appconfig.store";
    private static final String APPCONFIGTABLE = "appconfigtable";
    private static final String ID = "id";
    private static final String PACKAGE_NAME = "packagename";
    private static final String SESSION_START_TIME = "sessionstarttime";
    private static final String SESSION_END_TIME = "sessionendtime";
    private static final String SESSION_ALLOWED_DURATION = "sessionallowedduration";
    private static final String SESSION_STATUS = "sessionstatus";
    private static final String SESSION_TASK_ID = "sessiontaskid";

    private static final String COURSETABLE = "tablecourse";
    private static final String COURSE_ID = "courseid";
    private static final String QUESTION_ID = "questionid";
    private static final String QUESTION_TEXT = "questiontest";
    private static final String QUESTION_TYPE = "questiontype";
    private static final String MCQ_OPTIONS = "mcqoptions";
    private static final String SOLUTION = "solution";

    public SqliteAppConfigStore(Context context, int version) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CreateAppConfigTable = "CREATE TABLE " + APPCONFIGTABLE + "("
                + ID + " INTEGER PRIMARY KEY,"
                + PACKAGE_NAME + " TEXT,"
                + SESSION_START_TIME + " INTEGER,"
                + SESSION_END_TIME + " INTEGER,"
                + SESSION_ALLOWED_DURATION + " INTEGER,"
                + SESSION_STATUS + " INTEGER,"
                + SESSION_TASK_ID + " TEXT"
                + ")";

        final String CreateCourseTable = "CREATE TABLE " + COURSETABLE + "("
                + COURSE_ID + " TEXT PRIMARY KEY,"
                + QUESTION_ID + " TEXT,"
                + QUESTION_TEXT + " TEXT,"
                + QUESTION_TYPE + " INTEGER,"
                + MCQ_OPTIONS + " BLOB,"
                + SOLUTION + " INTEGER"
                + ")";

        db.execSQL(CreateAppConfigTable);
        db.execSQL(CreateCourseTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + APPCONFIGTABLE);
        onCreate(db);
    }

    public AppConfig getAppConfig(String packageName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + APPCONFIGTABLE + " WHERE "
                + PACKAGE_NAME + "='" + packageName + "'" , null);
        List<AppSessionConfig> appSessionConfigList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                long sessionStartTime = cursor.getLong(cursor.getColumnIndex(SESSION_START_TIME));
                long sessionEndTime = cursor.getLong(cursor.getColumnIndex(SESSION_END_TIME));
                long sessionAllowedDuration = cursor.getLong(cursor.getColumnIndex(SESSION_ALLOWED_DURATION));
                int sessionStatus = cursor.getInt(cursor.getColumnIndex(SESSION_STATUS));
                AppStatus appStatus = sessionStatus > 0 ? AppStatus.ALLOWED : AppStatus.BLOCKED;
                appSessionConfigList.add(new AppSessionConfig(sessionStartTime, sessionEndTime,
                        sessionAllowedDuration, appStatus));
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
            db.insert(APPCONFIGTABLE, null, contentValues);
        }
    }

    public void deleteAppConfig(String packageName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(APPCONFIGTABLE, PACKAGE_NAME + "='" + packageName + "'", null);
    }

    public void insertOrUpdateCourse(LocalCourse course) {
        SQLiteDatabase db = this.getWritableDatabase();
        String courseId = course.getCourseId();
        deleteCourse(courseId);
        List<QuestionDetails> questionDetailsList = course.getQuestionDetailsList();
        for (QuestionDetails questionDetails : questionDetailsList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COURSE_ID, courseId);
            contentValues.put(QUESTION_ID, questionDetails.questionId);
            contentValues.put(QUESTION_TEXT, questionDetails.questionText);
            contentValues.put(QUESTION_TYPE, (questionDetails.questionType.equals(QuestionType.MCQ) ? 1 : -1));
            Gson gson = new Gson();
            contentValues.put(MCQ_OPTIONS, gson.toJson(questionDetails.options).getBytes());
            contentValues.put(SOLUTION, questionDetails.solution);
            db.insert(COURSETABLE, null, contentValues);
        }
    }

    public LocalCourse getCourse(String courseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + COURSETABLE + " WHERE "
                + COURSE_ID + "='" + courseId + "'" , null);
        List<QuestionDetails> questionDetailsList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String localQuestionId = cursor.getString(cursor.getColumnIndex(QUESTION_ID));
                String localQuestionText = cursor.getString(cursor.getColumnIndex(QUESTION_TEXT));
                int questionType = cursor.getInt(cursor.getColumnIndex(QUESTION_TYPE));
                QuestionType localQuestionType = questionType > 0 ? QuestionType.MCQ : QuestionType.SUBJECTIVE;
                int solution = cursor.getInt(cursor.getColumnIndex(SOLUTION));
                byte[] blob = cursor.getBlob(cursor.getColumnIndex(MCQ_OPTIONS));
                String json = new String(blob);
                Gson gson = new Gson();
                ArrayList<McqOptions> mcqOptionsArrayList = gson.fromJson(json, new TypeToken<ArrayList<McqOptions>>()
                {}.getType());
                questionDetailsList.add(new QuestionDetails(localQuestionId, localQuestionText, localQuestionType,
                        mcqOptionsArrayList, solution));
            } while (cursor.moveToNext());
        }
        LocalCourse localCourse = new LocalCourse(courseId, questionDetailsList);
        return localCourse;
    }

    public void deleteCourse(String courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(COURSETABLE, COURSE_ID + "='" + courseId + "'", null);
    }
}

