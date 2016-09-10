package com.acubeapps.storage.dynamodb.dao;

import java.util.List;

import com.acubeapps.storage.dynamodb.config.DynamoDbConfigProvider;
import com.acubeapps.storage.dynamodb.pojo.ChildLoginDetails;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;

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

	public ChildLoginDetails getByChildId(String childId) {
	    ChildLoginDetails details = new ChildLoginDetails();
	    details.setChildId(childId);
	    List<ChildLoginDetails> returnList = 
	        mapper.query(ChildLoginDetails.class, new DynamoDBQueryExpression<ChildLoginDetails>()
	    		.withIndexName("childId-index")
	    		.withHashKeyValues(details)
	    		.withConsistentRead(false));
	    if (returnList != null) {
	    	return returnList.get(0);
	    }

	    return null;
	}
}