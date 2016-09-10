package com.acubeapps.childconnect.events;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class ShowTaskScreenEvent {
    private String packageName;
    private String courseId;

    public ShowTaskScreenEvent(String packageName, String courseId) {
        this.packageName = packageName;
        this.courseId = courseId;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getCourseId() {
        return courseId;
    }
}
