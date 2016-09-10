package com.acubeapps.childconnect.utils;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.acubeapps.childconnect.Constants;
import com.acubeapps.childconnect.gcm.RegistrationIntentService;
import com.acubeapps.childconnect.model.AppUsage;
import com.acubeapps.childconnect.service.CoreService;
import com.acubeapps.childconnect.service.DeviceSyncService;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class Device {
    private static String lastKnownRunningPackage = null;

    public static List<String> getInstalledPackageNameList(Context context) {
        final List<String> apps = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packs = packageManager.getInstalledPackages(0);
        for(int i=0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            ApplicationInfo a = p.applicationInfo;
            if((a.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                continue;
            }
            apps.add(p.packageName);
        }
        return apps;
    }

    public static String getRunningPackageName(Context context) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        long endTime = System.currentTimeMillis();
        long startTime = endTime - TimeUnit.MINUTES.toMillis(10);
        UsageEvents usageEventsList = usageStatsManager.queryEvents(startTime, endTime);
        if (usageEventsList != null) {
            long timeStamp = 0;
            while (usageEventsList.hasNextEvent()) {
                UsageEvents.Event eventAux = new UsageEvents.Event();
                usageEventsList.getNextEvent(eventAux);
                if (eventAux.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND
                        && eventAux.getTimeStamp() > timeStamp) {
                    lastKnownRunningPackage = eventAux.getPackageName();
                    timeStamp = eventAux.getTimeStamp();
                }
            }
        }
        return lastKnownRunningPackage;
    }

    public static long computeAppUsage(Context context, String packageName, long startTime, long endTime) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        UsageEvents usageEvents = usageStatsManager.queryEvents(startTime, endTime);
        long foregroundTimeStamp = -1;
        long backgroundTimeStamp = -1;
        long timeInForeground = 0;
        while (usageEvents.hasNextEvent()) {
            UsageEvents.Event event = new UsageEvents.Event();
            usageEvents.getNextEvent(event);
            if (event.getPackageName().equals(packageName) && event.getTimeStamp() >= startTime) {
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                    backgroundTimeStamp = event.getTimeStamp();
                    long diff = backgroundTimeStamp - (foregroundTimeStamp > 0 ? foregroundTimeStamp : startTime);
                    timeInForeground = timeInForeground + diff;
                } else if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    foregroundTimeStamp = event.getTimeStamp();
                }
            }
        }
        if (foregroundTimeStamp > backgroundTimeStamp) {
            long diff = endTime - foregroundTimeStamp;
            timeInForeground = timeInForeground + diff;
        }
        return timeInForeground;
    }

    public static List<AppUsage> getAppUsage(Context context, long startTime, long endTime) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        PackageManager packageManager = context.getPackageManager();
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                startTime, endTime);
        List<AppUsage> appUsageList = new ArrayList<>();
        for (UsageStats usageStats : usageStatsList) {
            String appName = usageStats.getPackageName();
            String appDisplayName = "";
            try {
                appDisplayName = (packageManager.getApplicationLabel(packageManager.getApplicationInfo(appName, 0))).toString();
            } catch (Exception e) {

            }
            appUsageList.add(new AppUsage(appName, appDisplayName, null, usageStats.getTotalTimeInForeground()));
        }
        return appUsageList;
    }

    public static void initializeService(Context context, SharedPreferences preferences) {
        Intent serviceIntent = new Intent(context, CoreService.class);
        context.startService(serviceIntent);

        Intent syncServiceIntent = new Intent(context, DeviceSyncService.class);
        context.startService(syncServiceIntent);

        String registrationId = preferences.getString(Constants.GCM_REGISTRATION_ID, null);
        boolean isRegisteredWithServer = preferences.getBoolean(Constants.GCM_SERVER_SYNC_DONE, false);
        if (!isRegisteredWithServer || registrationId == null) {
            Intent gcmRegisterIntent = new Intent(context, RegistrationIntentService.class);
            context.startService(gcmRegisterIntent);
        }
    }

    public static void initializeDeviceSyncService(Context context) {
        Intent syncServiceIntent = new Intent(context, DeviceSyncService.class);
        context.startService(syncServiceIntent);
    }

    public static String uploadImageToServer(Context context, File file) {
        AWSCredentials credentials = new BasicAWSCredentials(Constants.MY_ACCESS_KEY_ID, Constants.MY_SECRET_KEY);
        AmazonS3 s3 = new AmazonS3Client(credentials);
        s3.setEndpoint("https://s3-ap-southeast-1.amazonaws.com/");
        TransferUtility transferUtility = new TransferUtility(s3, context);
        TransferObserver observer = transferUtility.upload(Constants.MY_BUCKET, file.getName(), file);
        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int i, TransferState transferState) {
                if (transferState.equals(TransferState.COMPLETED)) {
                    Log.d(Constants.LOG_TAG, "upload completed for image");
                }
            }

            @Override
            public void onProgressChanged(int i, long l, long l1) {

            }

            @Override
            public void onError(int i, Exception e) {

            }
        });
        String path = "https://" + Constants.MY_BUCKET + ".s3.amazonaws.com/" + file.getName();
        return path;
    }
}
