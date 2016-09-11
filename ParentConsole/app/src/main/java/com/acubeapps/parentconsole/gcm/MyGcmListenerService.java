package com.acubeapps.parentconsole.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.acubeapps.parentconsole.Constants;
import com.acubeapps.parentconsole.Injectors;
import com.acubeapps.parentconsole.PerformanceActivity;
import com.acubeapps.parentconsole.R;
import com.acubeapps.parentconsole.model.ChildCourseResult;
import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;

import java.net.URL;

import javax.inject.Inject;

/**
 * Created by ajitesh.shukla on 9/10/16.
 */
public class MyGcmListenerService extends GcmListenerService {

    @Inject
    SharedPreferences sharedPreferences;

    public MyGcmListenerService(){
        Injectors.appComponent().injectGCMService(this);
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("course");
        Log.d(Constants.LOG_TAG, "From: " + from);
        Log.d(Constants.LOG_TAG, "Message: " + message);
        sendNotification(message);
        // [END_EXCLUDE]
    }

    private void sendNotification(String message) {
        ChildCourseResult result = new Gson().fromJson(message, ChildCourseResult.class);
        String childName = sharedPreferences.getString(result.childId+"", null);
        Intent intent = new Intent(this, PerformanceActivity.class);
        intent.putExtra(Constants.CHILD_RESULT, result);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Course Completed")
                .setContentText(childName + " just completed a course. Tap to see his progress")
                .setAutoCancel(true);

        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);;
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
