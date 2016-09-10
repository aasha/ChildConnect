package com.acubeapps.service.pojo;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

@DynamoDBDocument
public class AppConfig {
    private String appName;
    private List<AppSessionConfig> appSessionConfigList;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public List<AppSessionConfig> getAppSessionConfigList() {
        return appSessionConfigList;
    }

    public void setAppSessionConfigList(List<AppSessionConfig> appSessionConfigList) {
        this.appSessionConfigList = appSessionConfigList;
    }
}
