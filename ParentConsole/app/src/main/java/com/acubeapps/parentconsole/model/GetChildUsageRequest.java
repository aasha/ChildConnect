package com.acubeapps.parentconsole.model;

/**
 * Created by aasha.medhi on 9/11/16.
 */
public class GetChildUsageRequest {
    public String childId;
    public String uploadTime;

    public GetChildUsageRequest(String childId, String uploadTime) {
        this.childId = childId;
        this.uploadTime = uploadTime;
    }
}
