package com.acubeapps.parentconsole.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.acubeapps.parentconsole.Constants;

/**
 * Created by ajitesh.shukla on 9/10/16.
 */
public class GcmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(Constants.LOG_TAG, "GCM token received");
        Log.d(Constants.LOG_TAG, "GCM intent - " + intent.toString());

        //do download app usage data and check for results from child
    }
}
