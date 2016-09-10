package com.acubeapps.childconnect.events;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class ShowTaskScreenEvent {
    private String packageName;

    public ShowTaskScreenEvent(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

}
