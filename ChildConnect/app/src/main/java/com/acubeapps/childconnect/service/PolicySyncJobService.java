package com.acubeapps.childconnect.service;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.util.Log;

import com.acubeapps.childconnect.Constants;
import com.acubeapps.childconnect.Injectors;
import com.acubeapps.childconnect.helpers.AppPolicyManager;
import com.acubeapps.childconnect.model.AppConfig;
import com.acubeapps.childconnect.model.GetUsageConfigResponse;
import com.acubeapps.childconnect.model.Policy;
import com.acubeapps.childconnect.network.NetworkInterface;
import com.acubeapps.childconnect.network.NetworkResponse;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;

/**
 * Created by ajitesh.shukla on 9/10/16.
 */
public class PolicySyncJobService extends JobService {

    @Inject
    SharedPreferences preferences;

    @Inject
    NetworkInterface networkInterface;

    @Inject
    AppPolicyManager appPolicyManager;

    public PolicySyncJobService() {
        Injectors.appComponent().injectPolicySyncJobService(this);
    }


    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        String childId = preferences.getString(Constants.CHILD_ID, null);
        Log.d(Constants.LOG_TAG, "downloading app usage policy");
        networkInterface.getUsageConfig(childId, new NetworkResponse<GetUsageConfigResponse>() {
            @Override
            public void success(GetUsageConfigResponse getUsageConfigResponse, Response response) {
                Log.d(Constants.LOG_TAG, "app policy received");
                Policy policy = getUsageConfigResponse.policy;
                String courseId = policy.courseId;
                preferences.edit().putString(Constants.COURSE_ID, courseId).apply();
                List<AppConfig> appConfigList = policy.appConfigList;
                for (AppConfig appConfig : appConfigList) {
                    appPolicyManager.storeAppConfig(appConfig);
                }
                startCourseSyncJob();
                PolicySyncJobService.this.jobFinished(jobParameters, false);
            }

            @Override
            public void failure(GetUsageConfigResponse getUsageConfigResponse) {
                Log.d(Constants.LOG_TAG, "failed to download app usgae policy");
                PolicySyncJobService.this.jobFinished(jobParameters, true);
            }

            @Override
            public void networkFailure(Throwable error) {
                Log.d(Constants.LOG_TAG, "failed to download app usgae policy due to network failure");
                PolicySyncJobService.this.jobFinished(jobParameters, true);
            }
        });
        return true;
    }

    private void startCourseSyncJob() {
        JobScheduler jobScheduler = (JobScheduler) this.getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName courseSyncComponent = new ComponentName(this, CourseSyncJobService.class);
        JobInfo policyJobInfo = new JobInfo.Builder(getMaxPendingId() + 1, courseSyncComponent)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();
        jobScheduler.schedule(policyJobInfo);
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

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
