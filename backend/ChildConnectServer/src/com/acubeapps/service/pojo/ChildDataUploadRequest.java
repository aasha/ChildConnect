package com.acubeapps.service.pojo;

import java.util.List;

public class ChildDataUploadRequest {
    private String childId;
    private String uploadTime;
    private List<String> browserHistory;
    private List<AppUsage> appUsage;

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public List<String> getBrowserHistory() {
        return browserHistory;
    }

    public void setBrowserHistory(List<String> browserHistory) {
        this.browserHistory = browserHistory;
    }

    public List<AppUsage> getAppUsage() {
        return appUsage;
    }

    public void setAppUsage(List<AppUsage> appUsage) {
        this.appUsage = appUsage;
    }
}
