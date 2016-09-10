package com.acubeapps.storage.dynamodb.dao;

import com.acubeapps.storage.dynamodb.config.DynamoDbConfigProvider;
import com.acubeapps.storage.dynamodb.pojo.ChildPolicyDetails;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class ChildPolicyDao {
	private DynamoDBMapper mapper;

	public ChildPolicyDao() {
		mapper = new DynamoDBMapper(DynamoDbConfigProvider.getDynamoDb());
	}

	public void save(ChildPolicyDetails pojo) {
		mapper.save(pojo);
	}

	public ChildPolicyDetails get(String childId) {
		ChildPolicyDetails details = new ChildPolicyDetails();
		details.setChildId(childId);
		return mapper.load(details);
	}
}