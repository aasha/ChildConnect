package com.acubeapps.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.acubeapps.service.pojo.AppConfig;
import com.acubeapps.service.pojo.ChildDataUploadRequest;
import com.acubeapps.service.pojo.ChildDataUploadResponse;
import com.acubeapps.service.pojo.ChildPolicyUploadRequest;
import com.acubeapps.service.pojo.ChildPolicyUploadResponse;
import com.acubeapps.service.pojo.Policy;
import com.acubeapps.storage.dynamodb.dao.ChildDataUsageDao;
import com.acubeapps.storage.dynamodb.dao.ChildPolicyDao;
import com.acubeapps.storage.dynamodb.pojo.ChildDataUsageDetails;
import com.acubeapps.storage.dynamodb.pojo.ChildPolicyDetails;

@Path("/upload")
public class UploadService {

    private final ChildDataUsageDao childDataUsageDao;
    private final ChildPolicyDao childPolicyDao;

    public UploadService() {
        childDataUsageDao = new ChildDataUsageDao();
        childPolicyDao = new ChildPolicyDao();
    }

    @POST
    @Path("/childDataUsage")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ChildDataUploadResponse uploadChildDataUsage(final ChildDataUploadRequest request) {
        ChildDataUsageDetails details = new ChildDataUsageDetails();
        details.setChildId(request.getChildId());
        details.setUploadTime(request.getUploadTime());
        details.setUsageDetails(request.getAppUsage());
        childDataUsageDao.save(details);

        ChildDataUploadResponse response = new ChildDataUploadResponse();
        response.setStatus("success");
        return response;
    }

    @POST
    @Path("/childPolicy")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ChildPolicyUploadResponse uploadChildPolicy(final ChildPolicyUploadRequest request) {
        ChildPolicyDetails details = new ChildPolicyDetails();
        details.setChildId(request.getChildId());
//        Policy policy = new Policy();
//        policy.setCourseId("abc");
//        List<AppConfig> appConfigList = new ArrayList<>();
//        AppConfig appConfig = new AppConfig();
//        appConfig.setAppName("com.facebook.katana");
//        appConfig.setStartTime("0");
//        appConfig.setEndTime(String.valueOf(TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)));
//        appConfig.setDuration(String.valueOf(TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)));
//        appConfigList.add(appConfig);
//        policy.setAppConfigList(appConfigList);
//        details.setPolicy(policy);
        details.setPolicy(request.getPolicy());
        childPolicyDao.save(details);

        ChildPolicyUploadResponse response = new ChildPolicyUploadResponse();
        response.setStatus("success");
        return response;
    }
}
