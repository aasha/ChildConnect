package com.acubeapps.parentconsole.model;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class GetSolutionRequest {
    public String courseId;
    public String questionId;
    public String solutionUrl;

    public GetSolutionRequest(String courseId, String questionId, String solutionUrl) {
        this.courseId = courseId;
        this.questionId = questionId;
        this.solutionUrl = solutionUrl;
    }
}
