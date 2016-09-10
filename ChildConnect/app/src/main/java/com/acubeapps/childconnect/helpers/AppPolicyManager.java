package com.acubeapps.childconnect.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.acubeapps.childconnect.Constants;
import com.acubeapps.childconnect.events.ChildRegisteredEvent;
import com.acubeapps.childconnect.model.AppConfig;
import com.acubeapps.childconnect.model.AppSessionConfig;
import com.acubeapps.childconnect.model.LocalCourse;
import com.acubeapps.childconnect.store.SqliteAppConfigStore;
import com.acubeapps.childconnect.utils.CommonUtils;
import com.acubeapps.childconnect.utils.Device;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class AppPolicyManager {
    private SqliteAppConfigStore appConfigStore;
    private EventBus eventBus;
    private Context context;
    private SharedPreferences preferences;

    public AppPolicyManager(SqliteAppConfigStore appConfigStore, EventBus eventBus, Context context,
                            SharedPreferences preferences) {
        this.appConfigStore = appConfigStore;
        this.eventBus = eventBus;
        this.context = context;
        this.eventBus.register(this);
        this.preferences = preferences;
    }

    public AppSessionConfig getActiveAppPolicy(String packageName) {
        AppConfig appConfig = appConfigStore.getAppConfig(packageName);
        List<AppSessionConfig> appSessionConfigList = appConfig.getAppSessionConfigList();
        for (AppSessionConfig appSessionConfig : appSessionConfigList) {
            if (isSessionActive(appSessionConfig)) {
                return appSessionConfig;
            }
        }
        return null;
    }

    private boolean isSessionActive(AppSessionConfig appSessionConfig) {
        return (CommonUtils.getTimeSinceStartOfDay() > appSessionConfig.getSessionStartTime()
                && CommonUtils.getTimeSinceStartOfDay() < appSessionConfig.getSessionEndTime());
    }

    public LocalCourse getCourse() {
        String courseId = preferences.getString(Constants.COURSE_ID, null);
        return appConfigStore.getCourse(courseId);
    }

    public void storeAppConfig(AppConfig appConfig) {
        appConfigStore.insertOrUpdateAppConfig(appConfig);
    }

    public void storeCourseDetails(LocalCourse localCourse) {
        appConfigStore.insertOrUpdateCourse(localCourse);
    }

    @Subscribe
    public  void onChildRegisteredEvent(ChildRegisteredEvent childRegisteredEvent) {
        Log.d(Constants.LOG_TAG, "child registered");
        Device.initializeDeviceSyncService(context);
    }
}
