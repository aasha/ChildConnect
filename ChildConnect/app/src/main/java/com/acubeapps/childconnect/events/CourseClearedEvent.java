package com.acubeapps.childconnect.events;

/**
 * Created by ajitesh.shukla on 9/10/16.
 */
public class CourseClearedEvent {
    private String packageName;
    private long timestamp;

    public CourseClearedEvent(String packageName, long timestamp) {
        this.packageName = packageName;
        this.timestamp = timestamp;
    }

    public String getPackageName() {
        return packageName;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
