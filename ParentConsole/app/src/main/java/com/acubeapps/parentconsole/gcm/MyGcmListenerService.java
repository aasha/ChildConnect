package com.acubeapps.parentconsole.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.acubeapps.parentconsole.Constants;

/**
 * Created by ajitesh.shukla on 9/10/16.
 */
public class MyGcmListenerService extends IntentService {

    public MyGcmListenerService() {
        super("MyGcmListenerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(Constants.LOG_TAG, "GCM token received");
    }
}
