package com.acubeapps.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.acubeapps.service.pojo.ChildDataUsageDownloadRequest;
import com.acubeapps.service.pojo.ChildDataUsageDownloadResponse;
import com.acubeapps.service.pojo.ChildPolicyDownloadRequest;
import com.acubeapps.service.pojo.ChildPolicyDownloadResponse;
import com.acubeapps.service.pojo.Error;
import com.acubeapps.storage.dynamodb.dao.ChildDataUsageDao;
import com.acubeapps.storage.dynamodb.dao.ChildPolicyDao;
import com.acubeapps.storage.dynamodb.pojo.ChildDataUsageDetails;
import com.acubeapps.storage.dynamodb.pojo.ChildPolicyDetails;

@Path("/download")
public class DownloadService {

    private ChildPolicyDao childPolicyDao;
    private ChildDataUsageDao childDataUsageDao;

    public DownloadService() {
        childPolicyDao = new ChildPolicyDao();
        childDataUsageDao = new ChildDataUsageDao();
    }

    @POST
    @Path("/childPolicy")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ChildPolicyDownloadResponse downloadChildPolicy(ChildPolicyDownloadRequest request) {

        ChildPolicyDownloadResponse response = new ChildPolicyDownloadResponse();
        ChildPolicyDetails details = childPolicyDao.get(request.getChildId());
        if (details == null) {
            response.setStatus("failure");
            Error error = new Error();
            error.setCode(404);
            error.setMessage("no policy set for child");
            List<Error> errorList = new ArrayList<>();
            errorList.add(error);
            response.setErrorList(errorList);
        } else {
            response.setStatus("success");
            response.setPolicy(details.getPolicy());
        }

        return response;
    }

    @POST
    @Path("/childDataUsage")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ChildDataUsageDownloadResponse downloadChildDataUsage(ChildDataUsageDownloadRequest request) {

    	ChildDataUsageDownloadResponse response = new ChildDataUsageDownloadResponse();
        ChildDataUsageDetails details = childDataUsageDao.get(request.getChildId(), request.getUploadTime());
        if (details == null) {
            response.setStatus("failure");
            Error error = new Error();
            error.setCode(404);
            error.setMessage("no data usage available for child for this time");
            List<Error> errorList = new ArrayList<>();
            errorList.add(error);
            response.setErrorList(errorList);
        } else {
            response.setStatus("success");
            response.setAppUsage(details.getUsageDetails());
        }

        return response;
    }
}
