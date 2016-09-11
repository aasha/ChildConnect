package com.acubeapps.storage.dynamodb.dao;

import java.util.List;

import com.acubeapps.storage.dynamodb.config.DynamoDbConfigProvider;
import com.acubeapps.storage.dynamodb.pojo.ParentLoginDetails;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;

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

	public ParentLoginDetails getByParentId(String parentId) {
		ParentLoginDetails details = new ParentLoginDetails();
	    details.setParentId(parentId);
	    List<ParentLoginDetails> returnList = 
	        mapper.query(ParentLoginDetails.class, new DynamoDBQueryExpression<ParentLoginDetails>()
	    		.withIndexName("parentId-index")
	    		.withHashKeyValues(details)
	    		.withConsistentRead(false));
	    if (returnList != null) {
	    	return returnList.get(0);
	    }

	    return null;
	}
}
