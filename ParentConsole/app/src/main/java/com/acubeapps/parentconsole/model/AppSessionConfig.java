package com.acubeapps.parentconsole.model;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class AppSessionConfig {
    private long sessionStartTime;
    private long sessionEndTime;
    private long sessionAllowedDuration;
    private AppStatus status;
    private String taskId;

    public AppSessionConfig(long sessionStartTime, long sessionEndTime, long sessionAllowedDuration,
                            AppStatus status, String taskId) {
        this.sessionStartTime = sessionStartTime;
        this.sessionEndTime = sessionEndTime;
        this.sessionAllowedDuration = sessionAllowedDuration;
        this.status = status;
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }

    public long getSessionStartTime() {
        return sessionStartTime;
    }

    public long getSessionEndTime() {
        return sessionEndTime;
    }

    public long getSessionAllowedDuration() {
        return sessionAllowedDuration;
    }

    public AppStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "AppSessionConfig{" +
                "sessionStartTime=" + sessionStartTime +
                ", sessionEndTime=" + sessionEndTime +
                ", sessionAllowedDuration=" + sessionAllowedDuration +
                ", status=" + status +
                ", taskId='" + taskId + '\'' +
                '}';
    }
}
