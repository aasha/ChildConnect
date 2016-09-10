package com.acubeapps.parentconsole.model;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class ParentRegisterRequest {
    public String name;
    public String email;
    public String password;

    public ParentRegisterRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
