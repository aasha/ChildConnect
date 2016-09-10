package com.acubeapps.childconnect.gcm;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by ajitesh.shukla on 9/10/16.
 */
public class MyInstanceIdListenerService extends IntentService {

    public MyInstanceIdListenerService() {
        super("MyInstanceIdListenerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
