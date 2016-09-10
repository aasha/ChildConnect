package com.acubeapps.childconnect.helpers;

import com.acubeapps.childconnect.model.AppConfig;
import com.acubeapps.childconnect.model.AppSessionConfig;
import com.acubeapps.childconnect.model.AppStatus;
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
        appSessionConfigList.add(new AppSessionConfig(TimeUnit.HOURS.toMillis(0), TimeUnit.HOURS.toMillis(2),
                TimeUnit.MINUTES.toMillis(5), AppStatus.ALLOWED, "abc"));
        appConfigStore.insertOrUpdateAppConfig(new AppConfig("com.facebook.katana",
                appSessionConfigList));
    }
}
