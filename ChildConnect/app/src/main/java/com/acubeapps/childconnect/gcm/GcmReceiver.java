package com.acubeapps.childconnect.gcm;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.acubeapps.childconnect.Constants;
import com.acubeapps.childconnect.service.PolicySyncJobService;

import java.util.List;

/**
 * Created by ajitesh.shukla on 9/10/16.
 */
public class GcmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(Constants.LOG_TAG, "GCM token received");
        Log.d(Constants.LOG_TAG, "GCM intent - " + intent.toString());
        scheduleDownloadOfPolicy(context);
    }

    private void scheduleDownloadOfPolicy(Context context) {
        ComponentName policySyncComponent = new ComponentName(context, PolicySyncJobService.class);
        JobInfo policyJobInfo = new JobInfo.Builder(getMaxPendingId(context) + 1, policySyncComponent)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(policyJobInfo);
    }

    private int getMaxPendingId(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        List<JobInfo> jobInfoList = jobScheduler.getAllPendingJobs();
        int maxJobInfoId = 1;
        for (JobInfo jobInfo : jobInfoList) {
            if (jobInfo.getId() > maxJobInfoId) {
                maxJobInfoId = jobInfo.getId();
            }
        }
        return maxJobInfoId;
    }
}
