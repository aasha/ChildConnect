package com.acubeapps.parentconsole.model;

import java.util.List;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class AppConfig {
    private String appName;
    private List<AppSessionConfig> appSessionConfigList;

    public AppConfig(String appName, List<AppSessionConfig> appSessionConfigList) {
        this.appName = appName;
        this.appSessionConfigList = appSessionConfigList;
    }

    public String getAppName() {
        return appName;
    }

    public List<AppSessionConfig> getAppSessionConfigList() {
        return appSessionConfigList;
    }
}
