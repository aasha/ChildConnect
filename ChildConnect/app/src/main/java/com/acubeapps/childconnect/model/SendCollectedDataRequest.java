package com.acubeapps.childconnect.model;

import java.util.List;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class SendCollectedDataRequest {
    public String childId;
    public String uploadTime;
    public List<AppUsage> appUsage;
    public List<String> browserHistory;

    public SendCollectedDataRequest(String childId, String uploadTime, List<AppUsage> appUsage, List<String> browserHistory) {
        this.childId = childId;
        this.uploadTime = uploadTime;
        this.appUsage = appUsage;
        this.browserHistory = browserHistory;
    }
}
