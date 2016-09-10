package com.acubeapps.childconnect.model;

import java.util.List;

/**
 * Created by ajitesh.shukla on 9/10/16.
 */
public class LocalCourse {
    private String courseId;
    private List<QuestionDetails> questionDetailsList;

    public LocalCourse(String courseId, List<QuestionDetails> questionDetailsList) {
        this.courseId = courseId;
        this.questionDetailsList = questionDetailsList;
    }

    public String getCourseId() {
        return courseId;
    }

    public List<QuestionDetails> getQuestionDetailsList() {
        return questionDetailsList;
    }
}
