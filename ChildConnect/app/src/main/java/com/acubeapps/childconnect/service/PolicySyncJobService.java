package com.acubeapps.childconnect.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.util.Log;

import com.acubeapps.childconnect.Constants;
import com.acubeapps.childconnect.Injectors;
import com.acubeapps.childconnect.helpers.AppPolicyManager;
import com.acubeapps.childconnect.model.AppConfig;
import com.acubeapps.childconnect.model.GetUsageConfigResponse;
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
                List<AppConfig> appConfigList = getUsageConfigResponse.appConfigList;
                for (AppConfig appConfig : appConfigList) {
                    appPolicyManager.storeAppConfig(appConfig);
                }
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

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
