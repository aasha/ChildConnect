package com.acubeapps.childconnect.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.PersistableBundle;
import android.util.Log;

import com.acubeapps.childconnect.Constants;
import com.acubeapps.childconnect.utils.CommonUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class DeviceSyncService extends IntentService {

    public DeviceSyncService() {
        super("DeviceSyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(Constants.LOG_TAG, "onHandleIntent for devicesyncservice");
        ComponentName uploadServiceComponent = new ComponentName(this, UploadSyncJobService.class);
        ComponentName policySyncComponent = new ComponentName(this, PolicySyncJobService.class);
        PersistableBundle extras = new PersistableBundle();
        long startTime = CommonUtils.getStartOfTheDayTime() - TimeUnit.HOURS.toMillis(24);
        long endTime = CommonUtils.getStartOfTheDayTime();
        extras.putLong(Constants.JOB_START_TIME, startTime);
        extras.putLong(Constants.JOB_END_TIME, endTime);
        JobInfo uploadJobInfo = new JobInfo.Builder(getMaxPendingId() + 1, uploadServiceComponent)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .setExtras(extras)
                .build();
        JobScheduler jobScheduler = (JobScheduler) this.getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(uploadJobInfo);

        JobInfo policyJobInfo = new JobInfo.Builder(getMaxPendingId() + 1, policySyncComponent)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();
        jobScheduler.schedule(policyJobInfo);
        scheduleSelf();
    }

    private int getMaxPendingId() {
        JobScheduler jobScheduler = (JobScheduler) this.getSystemService(JOB_SCHEDULER_SERVICE);
        List<JobInfo> jobInfoList = jobScheduler.getAllPendingJobs();
        int maxJobInfoId = 1;
        for (JobInfo jobInfo : jobInfoList) {
            if (jobInfo.getId() > maxJobInfoId) {
                maxJobInfoId = jobInfo.getId();
            }
        }
        return maxJobInfoId;
    }

    private void scheduleSelf() {
        long scheduleTime = CommonUtils.getStartOfTheDayTime() + TimeUnit.HOURS.toMillis(24);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, DeviceSyncService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, scheduleTime, pendingIntent);
        Log.d(Constants.LOG_TAG, "scheduling next job on - " + scheduleTime);
    }
}
