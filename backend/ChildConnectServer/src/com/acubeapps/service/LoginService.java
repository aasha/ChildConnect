package com.acubeapps.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.acubeapps.service.pojo.ChildLoginRequest;
import com.acubeapps.service.pojo.ChildLoginResponse;
import com.acubeapps.service.pojo.ParentLoginRequest;
import com.acubeapps.service.pojo.ParentLoginResponse;

@Path("/login")
public class LoginService {

        @POST
        @Path("/parent")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
    public ParentLoginResponse parentLogin(final ParentLoginRequest loginRequest) {
                ParentLoginResponse response = new ParentLoginResponse();
                response.setUserId("testUserId");
                response.setStatus("success");
        return response;
    }

    public ChildLoginResponse childLogin(final ChildLoginRequest loginRequest) {
        ChildLoginResponse response = new ChildLoginResponse();
        response.setChildId("testChildId");
        response.setStatus("success");
        return response;
    }
}