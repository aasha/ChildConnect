package com.acubeapps.childconnect.model;

import java.util.List;

/**
 * Created by ajitesh.shukla on 9/11/16.
 */
public class SendCourseResult {
    public String childId;
    public String courseId;
    public List<QuestionDetails> questionList;

    public SendCourseResult(String childId, String courseId, List<QuestionDetails> questionList) {
        this.childId = childId;
        this.courseId = courseId;
        this.questionList = questionList;
    }

    public SendCourseResult() {
    }
}
