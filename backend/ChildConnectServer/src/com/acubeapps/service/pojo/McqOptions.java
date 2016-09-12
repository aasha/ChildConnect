package com.acubeapps.service.pojo;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

@DynamoDBDocument
public class McqOptions implements Serializable {
    private String optionSequence;
    private String optionText;

    public String getOptionSequence() {
        return optionSequence;
    }

    public void setOptionSequence(String optionSequence) {
        this.optionSequence = optionSequence;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }
}
