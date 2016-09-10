package com.acubeapps.childconnect.model;

import java.util.List;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class SendCollectedDataRequest {
    public AppUsage appUsage;
    public List<String> browserHistory;

    public SendCollectedDataRequest(AppUsage appUsage, List<String> browserHistory) {
        this.appUsage = appUsage;
        this.browserHistory = browserHistory;
    }
}
