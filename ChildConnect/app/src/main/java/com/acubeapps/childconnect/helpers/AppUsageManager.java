package com.acubeapps.childconnect.helpers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.acubeapps.childconnect.Constants;
import com.acubeapps.childconnect.events.ShowTaskScreenEvent;
import com.acubeapps.childconnect.model.AppSessionConfig;
import com.acubeapps.childconnect.model.AppStatus;
import com.acubeapps.childconnect.task.WaitTimerActivity;
import com.acubeapps.childconnect.utils.CommonUtils;
import com.acubeapps.childconnect.utils.Device;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import com.acubeapps.childconnect.events.ShowTaskScreenEvent;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class AppUsageManager {
    private AppPolicyManager appPolicyManager;
    private Context context;
    private EventBus eventBus;

    public AppUsageManager(AppPolicyManager appPolicyManager, Context context, EventBus eventBus) {
        this.appPolicyManager = appPolicyManager;
        this.context = context;
        this.eventBus = eventBus;
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
    public void onShowTaskScreenEvent(ShowTaskScreenEvent event) {
        Log.d(Constants.LOG_TAG, "show task screen event received");
        Intent dummyActivityIntent = new Intent(context, WaitTimerActivity.class);
        dummyActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(dummyActivityIntent);
    }
}
