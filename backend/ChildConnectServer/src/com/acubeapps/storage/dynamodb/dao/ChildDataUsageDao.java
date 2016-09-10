package com.acubeapps.storage.dynamodb.dao;

import com.acubeapps.storage.dynamodb.config.DynamoDbConfigProvider;
import com.acubeapps.storage.dynamodb.pojo.ChildDataUsageDetails;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class ChildDataUsageDao {
	private DynamoDBMapper mapper;

	public ChildDataUsageDao() {
		mapper = new DynamoDBMapper(DynamoDbConfigProvider.getDynamoDb());
	}

	public void save(ChildDataUsageDetails pojo) {
		mapper.save(pojo);
	}

	public ChildDataUsageDetails get(String childId, String uploadTime) {
		ChildDataUsageDetails details = new ChildDataUsageDetails();
		details.setChildId(childId);
		details.setUploadTime(uploadTime);
		return mapper.load(details);
	}
}
