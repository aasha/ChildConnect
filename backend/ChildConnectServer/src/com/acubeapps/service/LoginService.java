package com.acubeapps.service;

import java.util.Random;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.acubeapps.service.pojo.ChildLoginRequest;
import com.acubeapps.service.pojo.ChildLoginResponse;
import com.acubeapps.service.pojo.GsmRegisterRequest;
import com.acubeapps.service.pojo.GsmRegisterResponse;
import com.acubeapps.service.pojo.ParentLoginRequest;
import com.acubeapps.service.pojo.ParentLoginResponse;
import com.acubeapps.storage.dynamodb.dao.ChildLoginDetailsDao;
import com.acubeapps.storage.dynamodb.dao.ParentLoginDetailsDao;
import com.acubeapps.storage.dynamodb.pojo.ChildLoginDetails;
import com.acubeapps.storage.dynamodb.pojo.ParentLoginDetails;

@Path("/login")
public class LoginService {

    private final ParentLoginDetailsDao parentLoginDao;
    private final ChildLoginDetailsDao childLoginDao;

    public LoginService() {
		super();
		parentLoginDao = new ParentLoginDetailsDao();
		childLoginDao = new ChildLoginDetailsDao();
	}

    @POST
    @Path("/parent")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ParentLoginResponse parentLogin(final ParentLoginRequest loginRequest) {
        ParentLoginResponse response = new ParentLoginResponse();

        ParentLoginDetails details = parentLoginDao.get(loginRequest.getEmail());
        if (details == null) {
        	details = new ParentLoginDetails();
        	details.setEmail(loginRequest.getEmail());
        	details.setName(loginRequest.getName());
        	details.setPassword(loginRequest.getPassword());
        	details.setParentId(String.valueOf(new Random().nextInt(99999) + 10000));
        	parentLoginDao.save(details);
        	response.setStatus("success");
        	response.setUserId(details.getParentId());
        } else if (details.getPassword().equals(loginRequest.getPassword())){
        	response.setStatus("success");
        	response.setUserId(details.getParentId());
        } else {
        	response.setStatus("failure");
        }

        return response;
    }

    @POST
    @Path("/child")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ChildLoginResponse childLogin(final ChildLoginRequest loginRequest) {
        ChildLoginResponse response = new ChildLoginResponse();
        ChildLoginDetails details = childLoginDao.get(loginRequest.getEmail());
        if (details == null) {
        	details = new ChildLoginDetails();
        	details.setEmail(loginRequest.getEmail());
        	details.setName(loginRequest.getName());
        	details.setToken(loginRequest.getToken());
        	details.setChildId(UUID.randomUUID().toString());
        	details.setParentUserId(loginRequest.getParentUserId());
        	details.setSource(loginRequest.getSource());
        	childLoginDao.save(details);
        	response.setStatus("success");
        	response.setChildId(details.getChildId());
        } else {
        	response.setStatus("success");
        	response.setChildId(details.getChildId());
        }

        return response;
    }

    @POST
    @Path("/parentGcm")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public GsmRegisterResponse registerParentGsm(final GsmRegisterRequest loginRequest) {
    	GsmRegisterResponse response = new GsmRegisterResponse();

        ParentLoginDetails details = parentLoginDao.get(loginRequest.getEmail());
        if (details == null) {
            response.setStatus("failure");
        } else {
        	details.setGcmToken(loginRequest.getGcmToken());
        	parentLoginDao.save(details);
        	response.setStatus("success");
        }

        return response;
    }

    @POST
    @Path("/childGcm")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public GsmRegisterResponse childLogin(final GsmRegisterRequest loginRequest) {
    	GsmRegisterResponse response = new GsmRegisterResponse();

        ChildLoginDetails details = childLoginDao.get(loginRequest.getEmail());
        if (details == null) {
            response.setStatus("failure");
        } else {
        	details.setGcmToken(loginRequest.getGcmToken());
        	childLoginDao.save(details);
        	response.setStatus("success");
        }

        return response;
    }
}