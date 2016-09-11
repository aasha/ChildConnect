package com.acubeapps.parentconsole.model;

import java.util.List;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class AppConfig {
    public String appName;
    public String displayName;
    public List<AppSessionConfig> appSessionConfigList;

    public AppConfig(String appName, String displayName, List<AppSessionConfig> appSessionConfigList) {
        this.appName = appName;
        this.displayName = displayName;
        this.appSessionConfigList = appSessionConfigList;
    }

    public String getAppName() {
        return appName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<AppSessionConfig> getAppSessionConfigList() {
        return appSessionConfigList;
    }
}
