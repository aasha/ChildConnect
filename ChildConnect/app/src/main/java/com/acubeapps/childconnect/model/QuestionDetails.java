package com.acubeapps.childconnect.model;

import java.util.List;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class QuestionDetails {
    public String questionId;
    public String questionText;
    public String questionType;
    public List<McqOptions> options;
    public int solution;
}
