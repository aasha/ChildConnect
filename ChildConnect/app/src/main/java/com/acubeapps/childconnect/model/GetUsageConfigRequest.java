package com.acubeapps.childconnect.model;

import java.util.List;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class GetUsageConfigRequest {
    public String childId;
    public List<String> appList;

    public GetUsageConfigRequest(String childId, List<String> appList) {
        this.childId = childId;
        this.appList = appList;
    }
}
