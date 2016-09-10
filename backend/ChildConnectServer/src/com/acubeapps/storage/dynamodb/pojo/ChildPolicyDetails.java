package com.acubeapps.storage.dynamodb.pojo;

import com.acubeapps.service.pojo.Policy;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "ChildPolicyDetails")
public class ChildPolicyDetails {
    private String childId;
    private Policy policy;

    @DynamoDBHashKey(attributeName = "childId")
    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }
}