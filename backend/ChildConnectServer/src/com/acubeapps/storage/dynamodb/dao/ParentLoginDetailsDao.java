package com.acubeapps.storage.dynamodb.dao;

import com.acubeapps.storage.dynamodb.config.DynamoDbConfigProvider;
import com.acubeapps.storage.dynamodb.pojo.ParentLoginDetails;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class ParentLoginDetailsDao {
    private DynamoDBMapper mapper;

    public ParentLoginDetailsDao() {
        mapper = new DynamoDBMapper(DynamoDbConfigProvider.getDynamoDb());
    }

    public void save(ParentLoginDetails pojo) {
        mapper.save(pojo);
    }

    public ParentLoginDetails get(String email) {
        ParentLoginDetails details = new ParentLoginDetails();
        details.setEmail(email);
        return mapper.load(details);
    }
}
