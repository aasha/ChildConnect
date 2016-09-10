package com.acubeapps.parentconsole.network;

import retrofit2.Response;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public interface NetworkResponse<T> {
    /** Successful HTTP response. */
    void success(T t, Response response);
    /**
     * Failure  response
     */
    void failure(T t);
    /**
     * Unsuccessful HTTP response due to network failure, non-2XX status code, or unexpected
     * exception.
     */
    void networkFailure(Throwable error);
}
