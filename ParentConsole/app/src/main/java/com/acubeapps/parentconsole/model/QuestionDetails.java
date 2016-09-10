package com.acubeapps.parentconsole.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class QuestionDetails implements Serializable{
    public String questionId;
    public String questionText;
    public QuestionType questionType;
    public List<McqOptions> options;
    public int solution;

    public QuestionDetails(String questionId, String questionText, QuestionType questionType, List<McqOptions> options, int solution) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.questionType = questionType;
        this.options = options;
        this.solution = solution;
    }

    public QuestionDetails() {

    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public List<McqOptions> getOptions() {
        return options;
    }

    public void setOptions(List<McqOptions> options) {
        this.options = options;
    }

    public int getSolution() {
        return solution;
    }

    public void setSolution(int solution) {
        this.solution = solution;
    }
}
