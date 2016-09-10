package com.acubeapps.service.pojo;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

@DynamoDBDocument
public class Policy {
    private String courseId;
    private List<AppConfig> appConfigList;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public List<AppConfig> getAppConfigList() {
        return appConfigList;
    }

    public void setAppConfigList(List<AppConfig> appConfigList) {
        this.appConfigList = appConfigList;
    }
}
