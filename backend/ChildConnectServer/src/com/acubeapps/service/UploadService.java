package com.acubeapps.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import com.acubeapps.service.pojo.AppConfig;
import com.acubeapps.service.pojo.AppSessionConfig;
import com.acubeapps.service.pojo.ChildDataUploadRequest;
import com.acubeapps.service.pojo.ChildDataUploadResponse;
import com.acubeapps.service.pojo.ChildPolicyUploadRequest;
import com.acubeapps.service.pojo.ChildPolicyUploadResponse;
import com.acubeapps.service.pojo.Policy;
import com.acubeapps.storage.dynamodb.dao.ChildCourseDao;
import com.acubeapps.storage.dynamodb.dao.ChildDataUsageDao;
import com.acubeapps.storage.dynamodb.dao.ChildLoginDetailsDao;
import com.acubeapps.storage.dynamodb.dao.ChildPolicyDao;
import com.acubeapps.storage.dynamodb.dao.ParentLoginDetailsDao;
import com.acubeapps.storage.dynamodb.pojo.ChildCourseDetails;
import com.acubeapps.storage.dynamodb.pojo.ChildDataUsageDetails;
import com.acubeapps.storage.dynamodb.pojo.ChildPolicyDetails;
import com.acubeapps.utils.GcmNotificationSender;

@Path("/upload")
public class UploadService {

    private final ChildDataUsageDao childDataUsageDao;
    private final ChildPolicyDao childPolicyDao;
    private final ChildCourseDao childCourseDao;
    private final ParentLoginDetailsDao parentLoginDao;
    private final ChildLoginDetailsDao childLoginDao;

    public UploadService() {
        childDataUsageDao = new ChildDataUsageDao();
        childPolicyDao = new ChildPolicyDao();
        childCourseDao = new ChildCourseDao();
        parentLoginDao = new ParentLoginDetailsDao();
        childLoginDao = new ChildLoginDetailsDao();
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
        details.setBrowserHistory(request.getBrowserHistory());
        childDataUsageDao.save(details);

        String parentId = childLoginDao.getByChildId(request.getChildId()).getParentUserId();

        JSONObject object = new JSONObject();
        object.put("action", "ChildDataAvailable");
        GcmNotificationSender.sendGcm(parentLoginDao.getByParentId(parentId).getGcmToken(), object);
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
//        policy.setCourseId("b44fea93-664f-4854-9c16-0a1ea6f6f252");
//        List<AppConfig> appConfigList = new ArrayList<>();
//        List<AppSessionConfig> appSessionConfigList = new ArrayList<>();
//        AppSessionConfig appSessionConfig = new AppSessionConfig();
//        appSessionConfig.setSessionStartTime("0");
//        appSessionConfig.setSessionEndTime(String.valueOf(TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)));
//        appSessionConfig.setSessionAllowedDuration(String.valueOf(TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)));
//        appSessionConfig.setStatus("ALLOWED");
//        appSessionConfigList.add(appSessionConfig);
//
//        AppConfig appConfig = new AppConfig();
//        appConfig.setAppName("com.facebook.katana");
//        appConfig.setAppSessionConfigList(appSessionConfigList);
//
//        appConfigList.add(appConfig);
//        policy.setAppConfigList(appConfigList);
//        details.setPolicy(policy);


        details.setPolicy(request.getPolicy());
        childPolicyDao.save(details);

        ChildCourseDetails childCourseDetails = new ChildCourseDetails();
        childCourseDetails.setChildId(request.getChildId());
        childCourseDetails.setCompletionStatus("incomplete");
        childCourseDetails.setCourseId(request.getPolicy().getCourseId());
        //childCourseDetails.setCourseId(policy.getCourseId());

        childCourseDao.save(childCourseDetails);

        JSONObject object = new JSONObject();
        object.put("action", "PolicyAvailable");
        GcmNotificationSender.sendGcm(childLoginDao.getByChildId(request.getChildId()).getGcmToken(), object);

        ChildPolicyUploadResponse response = new ChildPolicyUploadResponse();
        response.setStatus("success");
        return response;
    }
}
