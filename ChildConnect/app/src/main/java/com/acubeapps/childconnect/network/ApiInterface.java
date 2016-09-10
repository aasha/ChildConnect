package com.acubeapps.childconnect.network;

import com.acubeapps.childconnect.model.BaseResponse;
import com.acubeapps.childconnect.model.ChildRegisterRequest;
import com.acubeapps.childconnect.model.ChildRegisterResponse;
import com.acubeapps.childconnect.model.GcmRegisterRequest;
import com.acubeapps.childconnect.model.GetAllCoursesRequest;
import com.acubeapps.childconnect.model.GetAllCoursesResponse;
import com.acubeapps.childconnect.model.GetCourseDetailsRequest;
import com.acubeapps.childconnect.model.GetCourseDetailsResponse;
import com.acubeapps.childconnect.model.GetSolutionRequest;
import com.acubeapps.childconnect.model.GetSolutionResponse;
import com.acubeapps.childconnect.model.GetUsageConfigRequest;
import com.acubeapps.childconnect.model.GetUsageConfigResponse;
import com.acubeapps.childconnect.model.ParentRegisterRequest;
import com.acubeapps.childconnect.model.RegisterResponse;
import com.acubeapps.childconnect.model.SendCollectedDataRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.POST;
/**
 * Created by aasha.medhi on 9/10/16.
 */
public interface ApiInterface {
    @POST("ChildConnectServer/login/parent")
    Call<RegisterResponse> register(@Body ParentRegisterRequest req);

    @POST("ChildConnectServer/login/child")
    Call<ChildRegisterResponse> registerChild(@Body ChildRegisterRequest req);

    @POST("ChildConnectServer/upload/childDataUsage")
    Call<BaseResponse> sendCollectedData(@Body SendCollectedDataRequest req);

    @POST("ChildConnectServer/download/childPolicy")
    Call<GetUsageConfigResponse> getUsageConfig(@Body GetUsageConfigRequest req);

    @POST("ChildConnectServer/getAllCourses")
    Call<GetAllCoursesResponse> getAllCourses(@Body GetAllCoursesRequest req);

    @POST("ChildConnectServer/getCourseDetails")
    Call<GetCourseDetailsResponse> getCourseDetails(@Body GetCourseDetailsRequest req);

    @POST("ChildConnectServer/getSolution")
    Call<GetSolutionResponse> getSolution(@Body GetSolutionRequest req);

    @POST("ChildConnectServer/login/childGcm")
    Call<BaseResponse> submitGcmToken(@Body GcmRegisterRequest req);
}
