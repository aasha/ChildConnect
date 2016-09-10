package com.acubeapps.childconnect.helpers;

import com.acubeapps.childconnect.model.AppConfig;
import com.acubeapps.childconnect.model.AppSessionConfig;
import com.acubeapps.childconnect.model.AppStatus;
import com.acubeapps.childconnect.model.LocalCourse;
import com.acubeapps.childconnect.model.McqOptions;
import com.acubeapps.childconnect.model.QuestionDetails;
import com.acubeapps.childconnect.model.QuestionType;
import com.acubeapps.childconnect.store.SqliteAppConfigStore;
import com.acubeapps.childconnect.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class AppPolicyManager {
    private SqliteAppConfigStore appConfigStore;

    public AppPolicyManager(SqliteAppConfigStore appConfigStore) {
        this.appConfigStore = appConfigStore;
        insertDummyConfig();
        insertDummyCourse();
    }

    public AppSessionConfig getActiveAppPolicy(String packageName) {
        AppConfig appConfig = appConfigStore.getAppConfig(packageName);
        List<AppSessionConfig> appSessionConfigList = appConfig.getAppSessionConfigList();
        for (AppSessionConfig appSessionConfig : appSessionConfigList) {
            if (isSessionActive(appSessionConfig)) {
                return appSessionConfig;
            }
        }
        return null;
    }

    private boolean isSessionActive(AppSessionConfig appSessionConfig) {
        return (CommonUtils.getTimeSinceStartOfDay() > appSessionConfig.getSessionStartTime()
                && CommonUtils.getTimeSinceStartOfDay() < appSessionConfig.getSessionEndTime());
    }

    private void insertDummyConfig() {
        List<AppSessionConfig> appSessionConfigList = new ArrayList<>();
        appSessionConfigList.add(new AppSessionConfig(TimeUnit.HOURS.toMillis(15), TimeUnit.HOURS.toMillis(18),
                TimeUnit.MINUTES.toMillis(2), AppStatus.ALLOWED, "abc"));
        appConfigStore.insertOrUpdateAppConfig(new AppConfig("com.facebook.katana",
                appSessionConfigList));
    }

    public LocalCourse getCourse(String courseId) {
        return appConfigStore.getCourse(courseId);
    }

    private void insertDummyCourse() {
        String courseId = "abc";
        List<QuestionDetails> questionDetailsList = new ArrayList<>();
        List<McqOptions> mcqOptionsList = new ArrayList<>();
        mcqOptionsList.add(new McqOptions(1, "one"));
        mcqOptionsList.add(new McqOptions(2, "two"));
        mcqOptionsList.add(new McqOptions(3, "three"));
        mcqOptionsList.add(new McqOptions(4, "four"));
        String questionText = "dummy question";
        questionDetailsList.add(new QuestionDetails("1", questionText, QuestionType.MCQ, mcqOptionsList, 1));
        appConfigStore.insertOrUpdateCourse(new LocalCourse(courseId, questionDetailsList));
    }
}
