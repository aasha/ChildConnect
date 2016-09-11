package com.acubeapps.parentconsole.model;

/**
 * Created by aasha.medhi on 9/11/16.
 */
public class SetUsageConfigRequest {
    public String childId;
    public String parentUserId;
    public Policy policy;

    public SetUsageConfigRequest(String childId, String parentUserId, Policy policy) {
        this.childId = childId;
        this.parentUserId = parentUserId;
        this.policy = policy;
    }
}
