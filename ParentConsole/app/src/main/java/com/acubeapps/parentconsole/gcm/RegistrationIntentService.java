package com.acubeapps.parentconsole.gcm;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.acubeapps.parentconsole.Constants;
import com.acubeapps.parentconsole.Injectors;
import com.acubeapps.parentconsole.event.GcmRegistrationCompleteEvent;
import com.acubeapps.parentconsole.model.BaseResponse;
import com.acubeapps.parentconsole.network.NetworkInterface;
import com.acubeapps.parentconsole.network.NetworkResponse;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import retrofit2.Response;

/**
 * Created by ajitesh.shukla on 9/10/16.
 */
public class RegistrationIntentService extends IntentService {

    @Inject
    SharedPreferences preferences;

    @Inject
    EventBus eventBus;

    @Inject
    NetworkInterface networkInterface;

    public RegistrationIntentService() {
        super("RegistrationIntentService");
        Injectors.appComponent().injectRegistrationIntentService(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(Constants.LOG_TAG, "onHandle RegistrationIntentService");
        try {
            String registrationId = preferences.getString(Constants.GCM_REGISTRATION_ID, null);
            if (registrationId == null) {
                Log.d(Constants.LOG_TAG, "gcm token is null");
                GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
                registrationId = gcm.register("773509619008");
                preferences.edit().putString(Constants.GCM_REGISTRATION_ID, registrationId).apply();
                Log.d(Constants.LOG_TAG, "gcm token received id is " + registrationId);
            }
            String email = preferences.getString(Constants.EMAIL, null);
            networkInterface.registerGcm(email, registrationId, new NetworkResponse<BaseResponse>() {
                @Override
                public void success(BaseResponse baseResponse, Response response) {
                    Log.d(Constants.LOG_TAG, "GCM token sent");
                    preferences.edit().putBoolean(Constants.GCM_SERVER_SYNC_DONE, true).apply();
                    eventBus.post(new GcmRegistrationCompleteEvent());
                }

                @Override
                public void failure(BaseResponse baseResponse) {
                    Log.d(Constants.LOG_TAG, "GCM token send failure");
                    scheduleSelf();
                }

                @Override
                public void networkFailure(Throwable error) {
                    Log.d(Constants.LOG_TAG, "GCM token send network failure");
                    scheduleSelf();
                }
            });
        } catch (Exception e) {
            Log.d(Constants.LOG_TAG, "scheduling self");
            scheduleSelf();
        }
    }

    private void scheduleSelf() {
        long scheduleTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(20);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, RegistrationIntentService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, scheduleTime, pendingIntent);
        Log.d(Constants.LOG_TAG, "scheduling next job on - " + scheduleTime);
    }
}
