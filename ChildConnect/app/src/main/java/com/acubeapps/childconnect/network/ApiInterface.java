package com.acubeapps.childconnect.network;

import com.acubeapps.childconnect.model.BaseResponse;
import com.acubeapps.childconnect.model.ChildRegisterRequest;
import com.acubeapps.childconnect.model.ChildRegisterResponse;
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
    @POST("/register")
    Call<RegisterResponse> register(@Body ParentRegisterRequest req);

    @POST("/registerChild")
    Call<ChildRegisterResponse> registerChild(@Body ChildRegisterRequest req);

    @POST("/sendCollectedData")
    Call<BaseResponse> sendCollectedData(@Body SendCollectedDataRequest req);

    @POST("/getUsageConfig")
    Call<GetUsageConfigResponse> getUsageConfig(@Body GetUsageConfigRequest req);

    @POST("/getAllCourses")
    Call<GetAllCoursesResponse> getAllCourses(@Body GetAllCoursesRequest req);

    @POST("/getCourseDetails")
    Call<GetCourseDetailsResponse> getCourseDetails(@Body GetCourseDetailsRequest req);

    @POST("/getSolution")
    Call<GetSolutionResponse> getSolution(@Body GetSolutionRequest req);
}
