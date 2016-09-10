package com.acubeapps.storage.dynamodb.pojo;

import java.util.List;

import com.acubeapps.service.pojo.AppUsage;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "ChildDataUsageDetails")
public class ChildDataUsageDetails {
    private String childId;
    private String uploadTime;
    private List<AppUsage> usageDetails;

    @DynamoDBHashKey(attributeName = "childId")
    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    @DynamoDBRangeKey(attributeName = "uploadTime")
    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public List<AppUsage> getUsageDetails() {
        return usageDetails;
    }

    public void setUsageDetails(List<AppUsage> usageDetails) {
        this.usageDetails = usageDetails;
    }
}
