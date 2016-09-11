package com.acubeapps.parentconsole.network;

import com.acubeapps.parentconsole.Constants;
import com.acubeapps.parentconsole.model.BaseResponse;
import com.acubeapps.parentconsole.model.GcmRegisterRequest;
import com.acubeapps.parentconsole.model.GetAllCoursesRequest;
import com.acubeapps.parentconsole.model.GetAllCoursesResponse;
import com.acubeapps.parentconsole.model.GetChildListRequest;
import com.acubeapps.parentconsole.model.GetChildListResponse;
import com.acubeapps.parentconsole.model.GetChildUsageRequest;
import com.acubeapps.parentconsole.model.GetChildUsageResponse;
import com.acubeapps.parentconsole.model.GetCourseDetailsRequest;
import com.acubeapps.parentconsole.model.GetCourseDetailsResponse;
import com.acubeapps.parentconsole.model.GetSolutionRequest;
import com.acubeapps.parentconsole.model.GetSolutionResponse;
import com.acubeapps.parentconsole.model.GetUsageConfigRequest;
import com.acubeapps.parentconsole.model.GetUsageConfigResponse;
import com.acubeapps.parentconsole.model.ParentRegisterRequest;
import com.acubeapps.parentconsole.model.ParentRegisterResponse;
import com.acubeapps.parentconsole.model.Policy;
import com.acubeapps.parentconsole.model.SetUsageConfigRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class NetworkInterface {
    private static ApiInterface networkInterface;

    private NetworkInterface() {
        networkInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    public static NetworkInterface getInstance() {
        return new NetworkInterface();
    }

    public void register(String name, String email, String password, final NetworkResponse<ParentRegisterResponse> networkResponse) {
        ParentRegisterRequest parentRegisterRequest = new ParentRegisterRequest(name, email,
                password);
        Call<ParentRegisterResponse> call = networkInterface.register(parentRegisterRequest);
        call.enqueue(new Callback<ParentRegisterResponse>() {
            @Override
            public void onResponse(Call<ParentRegisterResponse> call, Response<ParentRegisterResponse> response) {
                ParentRegisterResponse responseBody = response.body();
                if (responseBody.status.equalsIgnoreCase(Constants.SUCCESS)) {
                    networkResponse.success(responseBody, response);
                } else {
                    networkResponse.failure(responseBody);
                }
            }

            @Override
            public void onFailure(Call<ParentRegisterResponse> call, Throwable t) {
                networkResponse.networkFailure(t);
            }
        });
    }

    public void getChildList(String parentId, final NetworkResponse<GetChildListResponse> networkResponse) {
        GetChildListRequest getChildListRequest = new GetChildListRequest(parentId);
        Call<GetChildListResponse> call = networkInterface.getChildList(getChildListRequest);
        call.enqueue(new Callback<GetChildListResponse>() {
            @Override
            public void onResponse(Call<GetChildListResponse> call, Response<GetChildListResponse> response) {
                GetChildListResponse responseBody = response.body();
                if (responseBody.status.equalsIgnoreCase(Constants.SUCCESS)) {
                    networkResponse.success(responseBody, response);
                } else {
                    networkResponse.failure(responseBody);
                }
            }

            @Override
            public void onFailure(Call<GetChildListResponse> call, Throwable t) {
                networkResponse.networkFailure(t);
            }
        });
    }

    public void getChildUsageList(String childId, String uploadTime, final NetworkResponse<GetChildUsageResponse> networkResponse) {
        GetChildUsageRequest getChildUsageRequest = new GetChildUsageRequest(childId, uploadTime);
        Call<GetChildUsageResponse> call = networkInterface.getChildUsageList(getChildUsageRequest);
        call.enqueue(new Callback<GetChildUsageResponse>() {
            @Override
            public void onResponse(Call<GetChildUsageResponse> call, Response<GetChildUsageResponse> response) {
                GetChildUsageResponse responseBody = response.body();
                if (responseBody.status.equalsIgnoreCase(Constants.SUCCESS)) {
                    networkResponse.success(responseBody, response);
                } else {
                    networkResponse.failure(responseBody);
                }
            }

            @Override
            public void onFailure(Call<GetChildUsageResponse> call, Throwable t) {
                networkResponse.networkFailure(t);
            }
        });
    }

    public void getChildUsagePolicy(String parentId, final NetworkResponse<GetUsageConfigResponse> networkResponse) {
        GetUsageConfigRequest getChildUsagePolicyRequest = new GetUsageConfigRequest(parentId);
        Call<GetUsageConfigResponse> call = networkInterface.getChildUsagePolicy(getChildUsagePolicyRequest);
        call.enqueue(new Callback<GetUsageConfigResponse>() {
            @Override
            public void onResponse(Call<GetUsageConfigResponse> call, Response<GetUsageConfigResponse> response) {
                GetUsageConfigResponse responseBody = response.body();
                if (responseBody.status.equalsIgnoreCase(Constants.SUCCESS)) {
                    networkResponse.success(responseBody, response);
                } else {
                    networkResponse.failure(responseBody);
                }
            }

            @Override
            public void onFailure(Call<GetUsageConfigResponse> call, Throwable t) {
                networkResponse.networkFailure(t);
            }
        });
    }

    public void setChildUsagePolicy(String childId, String parentId, Policy policy, final NetworkResponse<BaseResponse> networkResponse) {
        SetUsageConfigRequest getChildListRequest = new SetUsageConfigRequest(childId, parentId, policy);
        Call<BaseResponse> call = networkInterface.setChildUsagePolicy(getChildListRequest);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse responseBody = response.body();
                if (responseBody.status.equalsIgnoreCase(Constants.SUCCESS)) {
                    networkResponse.success(responseBody, response);
                } else {
                    networkResponse.failure(responseBody);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                networkResponse.networkFailure(t);
            }
        });
    }


    public void registerGcm(final String email, final String gcmToken, final NetworkResponse<BaseResponse> networkResponse) {
        final GcmRegisterRequest gcmRegisterRequest = new GcmRegisterRequest(email, gcmToken);
        Call<BaseResponse> call = networkInterface.submitGcmToken(gcmRegisterRequest);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse responseBody = response.body();
                if (responseBody.status.equalsIgnoreCase(Constants.SUCCESS)) {
                    networkResponse.success(responseBody, response);
                } else {
                    networkResponse.failure(responseBody);
                }
            }

            public void onFailure(Call<BaseResponse> call, Throwable t) {
                networkResponse.networkFailure(t);
            }
        });
    }

    public void getAllCourses(String childId, final NetworkResponse<GetAllCoursesResponse> networkResponse) {
        GetAllCoursesRequest getAllCoursesRequest = new GetAllCoursesRequest(childId);
        Call<GetAllCoursesResponse> call = networkInterface.getAllCourses(getAllCoursesRequest);
        call.enqueue(new Callback<GetAllCoursesResponse>() {
            @Override
            public void onResponse(Call<GetAllCoursesResponse> call, Response<GetAllCoursesResponse> response) {
                GetAllCoursesResponse responseBody = response.body();
                if (responseBody.status.equals(Constants.SUCCESS)) {
                    networkResponse.success(responseBody, response);
                } else {
                    networkResponse.failure(responseBody);
                }
            }

            @Override
            public void onFailure(Call<GetAllCoursesResponse> call, Throwable t) {
                networkResponse.networkFailure(t);
            }
        });
    }

    public void getCourseDetails(String courseId, final NetworkResponse<GetCourseDetailsResponse> networkResponse) {
        GetCourseDetailsRequest getCourseDetailsRequest = new GetCourseDetailsRequest(courseId);
        Call<GetCourseDetailsResponse> call = networkInterface.getCourseDetails(getCourseDetailsRequest);
        call.enqueue(new Callback<GetCourseDetailsResponse>() {
            @Override
            public void onResponse(Call<GetCourseDetailsResponse> call, Response<GetCourseDetailsResponse> response) {
                GetCourseDetailsResponse responseBody = response.body();
                if (responseBody.status.equals(Constants.SUCCESS)) {
                    networkResponse.success(responseBody, response);
                } else {
                    networkResponse.failure(responseBody);
                }
            }

            @Override
            public void onFailure(Call<GetCourseDetailsResponse> call, Throwable t) {
                networkResponse.networkFailure(t);
            }
        });
    }

    public void getSolution(String courseId, String questionId, String solutionUrl,
                            final NetworkResponse<GetSolutionResponse> networkResponse) {
        GetSolutionRequest getSolutionRequest = new GetSolutionRequest(courseId, questionId, solutionUrl);
        Call<GetSolutionResponse> call = networkInterface.getSolution(getSolutionRequest);
        call.enqueue(new Callback<GetSolutionResponse>() {
            @Override
            public void onResponse(Call<GetSolutionResponse> call, Response<GetSolutionResponse> response) {
                GetSolutionResponse responseBody = response.body();
                if (responseBody.status.equals(Constants.SUCCESS)) {
                    networkResponse.success(responseBody, response);
                } else {
                    networkResponse.failure(responseBody);
                }
            }

            @Override
            public void onFailure(Call<GetSolutionResponse> call, Throwable t) {
                networkResponse.networkFailure(t);
            }
        });
    }
}
