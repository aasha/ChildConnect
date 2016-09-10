package com.acubeapps.childconnect.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.acubeapps.childconnect.Constants;
import com.acubeapps.childconnect.Injectors;
import com.acubeapps.childconnect.helpers.AppUsageManager;
import com.acubeapps.childconnect.utils.Device;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

public class CoreService extends Service {
    private Timer timer  =  new Timer();

    @Inject
    AppUsageManager appUsageManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        this.registerReceiver(localReceiver, intentFilter);
        Injectors.appComponent().injectCoreService(this);
        timer.cancel();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                String runningPackage = Device.getRunningPackageName(CoreService.this);
                appUsageManager.processAppUsage(runningPackage);
                Log.d(Constants.LOG_TAG, "running app - " + runningPackage);
            }
        }, 2000, 2000);
        return super.onStartCommand(intent, START_STICKY, startId);
    }

    private BroadcastReceiver localReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                timer.cancel();
                CoreService.this.unregisterReceiver(localReceiver);
                CoreService.this.stopSelf();
            }
        }
    };
}
