package com.acubeapps.childconnect.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.acubeapps.childconnect.Constants;
import com.acubeapps.childconnect.events.CourseClearedEvent;
import com.acubeapps.childconnect.events.ShowTaskScreenEvent;
import com.acubeapps.childconnect.model.AppSessionConfig;
import com.acubeapps.childconnect.model.AppStatus;
import com.acubeapps.childconnect.utils.CommonUtils;
import com.acubeapps.childconnect.utils.Device;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class AppUsageManager {
    private AppPolicyManager appPolicyManager;
    private Context context;
    private EventBus eventBus;
    private SharedPreferences preferences;

    public AppUsageManager(AppPolicyManager appPolicyManager, Context context, EventBus eventBus,
                           SharedPreferences preferences) {
        this.appPolicyManager = appPolicyManager;
        this.context = context;
        this.eventBus = eventBus;
        this.preferences = preferences;
        eventBus.register(this);
    }

    public void processAppUsage(String packageName) {
        AppSessionConfig sessionConfig = appPolicyManager.getActiveAppPolicy(packageName);
        if (sessionConfig == null) {
            Log.d(Constants.LOG_TAG, "app config is null");
            return;
        }
        if (sessionConfig.getStatus().equals(AppStatus.BLOCKED)) {
            Log.d(Constants.LOG_TAG, "app is blocked - " + packageName);
            eventBus.post(new ShowTaskScreenEvent(packageName, null));
        } else {
            long startTime = CommonUtils.getStartOfTheDayTime() + sessionConfig.getSessionStartTime();
            long lastTimeStamp = preferences.getLong(packageName, 0);
            startTime = startTime > lastTimeStamp ? startTime : lastTimeStamp;
            long endTime = System.currentTimeMillis();
            long sessionUsage = Device.computeAppUsage(context, packageName, startTime, endTime);
            Log.d(Constants.LOG_TAG, "computed usage - " + sessionUsage);
            Log.d(Constants.LOG_TAG, "allowed appUsage - " + sessionConfig.getSessionAllowedDuration());
            if (sessionUsage > sessionConfig.getSessionAllowedDuration()) {
                eventBus.post(new ShowTaskScreenEvent(packageName, null));
            }
        }
    }

    @Subscribe
    public void onCourseClearedEvent(CourseClearedEvent event) {
        Log.d(Constants.LOG_TAG, "course cleared setting preference");
        preferences.edit().putLong(event.getPackageName(), event.getTimestamp()).apply();
    }
}
