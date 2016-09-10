package com.acubeapps.parentconsole.model;

/**
 * Created by ajitesh.shukla on 9/10/16.
 */
public class GcmRegisterRequest {
    public String email;
    public String gcmToken;

    public GcmRegisterRequest(String email, String gcmToken) {
        this.email = email;
        this.gcmToken = gcmToken;
    }
}
