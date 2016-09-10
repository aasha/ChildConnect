package com.acubeapps.childconnect.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.util.Log;

import com.acubeapps.childconnect.Constants;
import com.acubeapps.childconnect.Injectors;
import com.acubeapps.childconnect.model.AppUsage;
import com.acubeapps.childconnect.model.BaseResponse;
import com.acubeapps.childconnect.network.NetworkInterface;
import com.acubeapps.childconnect.network.NetworkResponse;
import com.acubeapps.childconnect.utils.CommonUtils;
import com.acubeapps.childconnect.utils.Device;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import retrofit2.Response;

/**
 * Created by ajitesh.shukla on 9/10/16.
 */
public class UploadSyncJobService extends JobService {

    @Inject
    SharedPreferences preferences;

    @Inject
    NetworkInterface networkInterface;

    public UploadSyncJobService() {
        Injectors.appComponent().injectUploadSyncJobService(this);
    }

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        long backUpStartTime = CommonUtils.getStartOfTheDayTime() - TimeUnit.HOURS.toMillis(24);
        long backUpEndTime = CommonUtils.getStartOfTheDayTime();
        long startTime = jobParameters.getExtras().getLong(Constants.JOB_START_TIME, backUpStartTime);
        long endTime = jobParameters.getExtras().getLong(Constants.JOB_END_TIME, backUpEndTime);
        String childId = preferences.getString(Constants.CHILD_ID, null);
        Log.d(Constants.LOG_TAG, "uploading app usage data " + startTime + " : " + endTime);
        List<AppUsage> appUsageList = Device.getAppUsage(this, startTime, endTime);
        networkInterface.sendCollectedData(childId, String.valueOf(startTime), appUsageList, null, new NetworkResponse<BaseResponse>() {
            @Override
            public void success(BaseResponse baseResponse, Response response) {
                Log.d(Constants.LOG_TAG, "successfully uploaded usage data");
                UploadSyncJobService.this.jobFinished(jobParameters, false);
            }

            @Override
            public void failure(BaseResponse baseResponse) {
                Log.d(Constants.LOG_TAG, "failed to upload usage data");
                UploadSyncJobService.this.jobFinished(jobParameters, true);
            }

            @Override
            public void networkFailure(Throwable error) {
                Log.d(Constants.LOG_TAG, "failed to upload usage data due to network failure");
                UploadSyncJobService.this.jobFinished(jobParameters, true);
            }
        });
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
