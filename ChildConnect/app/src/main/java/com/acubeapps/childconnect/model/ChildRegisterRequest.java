package com.acubeapps.childconnect.model;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class ChildRegisterRequest {
    public String name;
    public String email;
    public String token;
    public String source;
    public String parentUserId;

    public ChildRegisterRequest(String name, String email, String token, String source, String parentUserId) {
        this.name = name;
        this.email = email;
        this.token = token;
        this.source = source;
        this.parentUserId = parentUserId;
    }
}
