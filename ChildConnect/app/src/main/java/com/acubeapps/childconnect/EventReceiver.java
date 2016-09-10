package com.acubeapps.childconnect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.acubeapps.childconnect.utils.Device;

import javax.inject.Inject;

public class EventReceiver extends BroadcastReceiver {

    @Inject
    SharedPreferences preferences;

    public EventReceiver() {
        Injectors.appComponent().injectEventReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)
                || intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Device.initializeService(context, preferences);
        }
    }
}
