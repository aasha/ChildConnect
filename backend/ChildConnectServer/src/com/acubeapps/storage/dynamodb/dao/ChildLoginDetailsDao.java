package com.acubeapps.storage.dynamodb.dao;

import com.acubeapps.storage.dynamodb.config.DynamoDbConfigProvider;
import com.acubeapps.storage.dynamodb.pojo.ChildLoginDetails;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class ChildLoginDetailsDao {
	private DynamoDBMapper mapper;

	public ChildLoginDetailsDao() {
		mapper = new DynamoDBMapper(DynamoDbConfigProvider.getDynamoDb());
	}

	public void save(ChildLoginDetails pojo) {
		mapper.save(pojo);
	}

	public ChildLoginDetails get(String email) {
		ChildLoginDetails details = new ChildLoginDetails();
		details.setEmail(email);
		return mapper.load(details);
	}
}