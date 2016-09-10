package com.acubeapps.childconnect.events;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class DismissTaskScreenEvent {
    private String packageName;

    public DismissTaskScreenEvent(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }
}
