package com.acubeapps.childconnect.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.util.Log;

import com.acubeapps.childconnect.Constants;
import com.acubeapps.childconnect.Injectors;
import com.acubeapps.childconnect.helpers.AppPolicyManager;
import com.acubeapps.childconnect.model.GetCourseDetailsResponse;
import com.acubeapps.childconnect.model.LocalCourse;
import com.acubeapps.childconnect.model.QuestionDetails;
import com.acubeapps.childconnect.network.NetworkInterface;
import com.acubeapps.childconnect.network.NetworkResponse;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;

/**
 * Created by ajitesh.shukla on 9/10/16.
 */
public class CourseSyncJobService extends JobService {
    @Inject
    SharedPreferences preferences;

    @Inject
    NetworkInterface networkInterface;

    @Inject
    AppPolicyManager appPolicyManager;

    public CourseSyncJobService() {
        Injectors.appComponent().injectCourseSyncJobService(this);
    }

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        final String courseId = preferences.getString(Constants.COURSE_ID, null);
        Log.d(Constants.LOG_TAG, "downloading course details");
        networkInterface.getCourseDetails(courseId, new NetworkResponse<GetCourseDetailsResponse>() {
            @Override
            public void success(GetCourseDetailsResponse getCourseDetailsResponse, Response response) {
                List<QuestionDetails> questionDetailsList = getCourseDetailsResponse.courseDetails;
                appPolicyManager.storeCourseDetails(new LocalCourse(courseId, questionDetailsList));
                CourseSyncJobService.this.jobFinished(jobParameters, false);
            }

            @Override
            public void failure(GetCourseDetailsResponse getUsageConfigResponse) {
                Log.d(Constants.LOG_TAG, "failed to download app usgae policy");
                CourseSyncJobService.this.jobFinished(jobParameters, true);
            }

            @Override
            public void networkFailure(Throwable error) {
                Log.d(Constants.LOG_TAG, "failed to download app usgae policy due to network failure");
                CourseSyncJobService.this.jobFinished(jobParameters, true);
            }
        });
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
