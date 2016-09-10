package com.acubeapps.childconnect.helpers;

import android.content.Context;

import com.acubeapps.childconnect.events.ChildRegisteredEvent;
import com.acubeapps.childconnect.model.AppConfig;
import com.acubeapps.childconnect.model.AppSessionConfig;
import com.acubeapps.childconnect.model.LocalCourse;
import com.acubeapps.childconnect.model.McqOptions;
import com.acubeapps.childconnect.model.QuestionDetails;
import com.acubeapps.childconnect.model.QuestionType;
import com.acubeapps.childconnect.store.SqliteAppConfigStore;
import com.acubeapps.childconnect.utils.CommonUtils;
import com.acubeapps.childconnect.utils.Device;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class AppPolicyManager {
    private SqliteAppConfigStore appConfigStore;
    private EventBus eventBus;
    private Context context;

    public AppPolicyManager(SqliteAppConfigStore appConfigStore, EventBus eventBus, Context context) {
        this.appConfigStore = appConfigStore;
        this.eventBus = eventBus;
        this.context = context;
        this.eventBus.register(this);
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

//<<<<<<< HEAD
//=======
//    private void insertDummyConfig() {
//        List<AppSessionConfig> appSessionConfigList = new ArrayList<>();
//        appSessionConfigList.add(new AppSessionConfig(TimeUnit.HOURS.toMillis(18), TimeUnit.HOURS.toMillis(22),
//                TimeUnit.MINUTES.toMillis(1), AppStatus.ALLOWED, "abc"));
//        appConfigStore.insertOrUpdateAppConfig(new AppConfig("com.facebook.katana",
//                appSessionConfigList));
//    }
//
//>>>>>>> b55ce2ebbd8f4072677144287d07a3345c48d64c
    public LocalCourse getCourse(String courseId) {
        return appConfigStore.getCourse(courseId);
    }

    public void storeAppConfig(AppConfig appConfig) {
        appConfigStore.insertOrUpdateAppConfig(appConfig);
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

    @Subscribe
    public  void onChildRegisteredEvent(ChildRegisteredEvent childRegisteredEvent) {
        Device.initializeDeviceSyncService(context);
    }
}
