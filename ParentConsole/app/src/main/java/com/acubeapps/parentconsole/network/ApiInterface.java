package com.acubeapps.parentconsole.network;

import com.acubeapps.parentconsole.model.BaseResponse;
import com.acubeapps.parentconsole.model.GcmRegisterRequest;
import com.acubeapps.parentconsole.model.GetAllCoursesRequest;
import com.acubeapps.parentconsole.model.GetAllCoursesResponse;
import com.acubeapps.parentconsole.model.GetCourseDetailsRequest;
import com.acubeapps.parentconsole.model.GetCourseDetailsResponse;
import com.acubeapps.parentconsole.model.GetSolutionRequest;
import com.acubeapps.parentconsole.model.GetSolutionResponse;
import com.acubeapps.parentconsole.model.ParentRegisterRequest;
import com.acubeapps.parentconsole.model.ParentRegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public interface ApiInterface {
    @POST("ChildConnectServer/login/parent")
    Call<ParentRegisterResponse> register(@Body ParentRegisterRequest req);

    @POST("ChildConnectServer/getAllCourses")
    Call<GetAllCoursesResponse> getAllCourses(@Body GetAllCoursesRequest req);

    @POST("ChildConnectServer/getCourseDetails")
    Call<GetCourseDetailsResponse> getCourseDetails(@Body GetCourseDetailsRequest req);

    @POST("ChildConnectServer/getSolution")
    Call<GetSolutionResponse> getSolution(@Body GetSolutionRequest req);

    @POST("ChildConnectServer/login/parentGcm")
    Call<BaseResponse> submitGcmToken(@Body GcmRegisterRequest req);
}
