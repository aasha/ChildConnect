package com.acubeapps.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.acubeapps.service.pojo.ChildDataUploadRequest;
import com.acubeapps.service.pojo.ChildDataUploadResponse;
import com.acubeapps.storage.dynamodb.dao.ChildDataUsageDao;
import com.acubeapps.storage.dynamodb.pojo.ChildDataUsageDetails;

@Path("/upload")
public class UploadService {

    private final ChildDataUsageDao childDataUsageDao;

    public UploadService() {
        childDataUsageDao = new ChildDataUsageDao();
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
}
