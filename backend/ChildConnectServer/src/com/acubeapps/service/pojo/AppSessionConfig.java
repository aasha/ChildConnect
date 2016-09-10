package com.acubeapps.service.pojo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

@DynamoDBDocument
public class AppSessionConfig {
    private String sessionStartTime;
    private String sessionEndTime;
    private String sessionAllowedDuration;
    private String status;

    public String getSessionStartTime() {
        return sessionStartTime;
    }

    public void setSessionStartTime(String sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }

    public String getSessionEndTime() {
        return sessionEndTime;
    }

    public void setSessionEndTime(String sessionEndTime) {
        this.sessionEndTime = sessionEndTime;
    }

    public String getSessionAllowedDuration() {
        return sessionAllowedDuration;
    }

    public void setSessionAllowedDuration(String sessionAllowedDuration) {
        this.sessionAllowedDuration = sessionAllowedDuration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
