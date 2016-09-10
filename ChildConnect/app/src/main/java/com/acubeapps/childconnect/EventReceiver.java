package com.acubeapps.childconnect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.acubeapps.childconnect.utils.Device;

public class EventReceiver extends BroadcastReceiver {
    public EventReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)
                || intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Device.initializeService(context);
        }
    }
}
