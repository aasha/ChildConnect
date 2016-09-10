package com.acubeapps.parentconsole.model;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class AppUsage {
    public String appName;
    public String displayName;
    public String appImageLink;
    public long duration;

    public AppUsage(String appName, String displayName, String appImageLink, long duration) {
        this.appName = appName;
        this.displayName = displayName;
        this.appImageLink = appImageLink;
        this.duration = duration;
    }
}
