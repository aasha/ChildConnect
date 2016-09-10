package com.acubeapps.childconnect.network;

import android.util.Log;

import com.acubeapps.childconnect.Constants;
import com.acubeapps.childconnect.model.AppUsage;
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

import java.util.List;

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

    public void register(String name, String email, String password, final NetworkResponse<RegisterResponse> networkResponse) {
        ParentRegisterRequest parentRegisterRequest = new ParentRegisterRequest(name, email,
                password);
        Call<RegisterResponse> call = networkInterface.register(parentRegisterRequest);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                RegisterResponse responseBody = response.body();
                if (responseBody.status.equals(Constants.SUCCESS)) {
                    networkResponse.success(responseBody, response);
                } else {
                    networkResponse.failure(responseBody);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                networkResponse.networkFailure(t);
            }
        });
    }

    public void registerChild(String name, String email, String token, String source, String parentUserId,
                              final NetworkResponse<ChildRegisterResponse> networkResponse) {
        ChildRegisterRequest childRegisterRequest = new ChildRegisterRequest(name, email, token, source,
                parentUserId);
        Call<ChildRegisterResponse> call = networkInterface.registerChild(childRegisterRequest);
        call.enqueue(new Callback<ChildRegisterResponse>() {
            @Override
            public void onResponse(Call<ChildRegisterResponse> call, Response<ChildRegisterResponse> response) {
                ChildRegisterResponse responseBody = response.body();
                if (responseBody.status.equalsIgnoreCase(Constants.SUCCESS)) {
                    networkResponse.success(responseBody, response);
                } else {
                    networkResponse.failure(responseBody);
                }
            }

            @Override
            public void onFailure(Call<ChildRegisterResponse> call, Throwable t) {
                networkResponse.networkFailure(t);
            }
        });
    }

    public void sendCollectedData(String childId, List<AppUsage> appUsage, List<String> browserHistory, final NetworkResponse<BaseResponse> networkResponse) {
        SendCollectedDataRequest sendCollectedDataRequest = new SendCollectedDataRequest(childId, appUsage,
                browserHistory);
        Call<BaseResponse> call = networkInterface.sendCollectedData(sendCollectedDataRequest);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse responseBody = response.body();
                if (responseBody.status.equals(Constants.SUCCESS)) {
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

    public void getUsageConfig(String childId, final NetworkResponse<GetUsageConfigResponse> networkResponse) {
        GetUsageConfigRequest getUsageConfigRequest = new GetUsageConfigRequest(childId);
        Call<GetUsageConfigResponse> call = networkInterface.getUsageConfig(getUsageConfigRequest);
        call.enqueue( new Callback<GetUsageConfigResponse>() {
            @Override
            public void onResponse(Call<GetUsageConfigResponse> call, Response<GetUsageConfigResponse> response) {
                GetUsageConfigResponse responseBody = response.body();
                if (responseBody.status.equals(Constants.SUCCESS)) {
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
