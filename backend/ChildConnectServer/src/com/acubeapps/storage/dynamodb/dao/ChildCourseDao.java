package com.acubeapps.storage.dynamodb.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.acubeapps.storage.dynamodb.config.DynamoDbConfigProvider;
import com.acubeapps.storage.dynamodb.pojo.ChildCourseDetails;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class ChildCourseDao {
	private DynamoDBMapper mapper;

	public ChildCourseDao() {
		mapper = new DynamoDBMapper(DynamoDbConfigProvider.getDynamoDb());
	}

	public void save(ChildCourseDetails pojo) {
		mapper.save(pojo);
	}

	public ChildCourseDetails get(String childId, String courseId) {
		ChildCourseDetails details = new ChildCourseDetails();
		details.setChildId(childId);
		details.setCourseId(courseId);
		return mapper.load(details);
	}

	public List<ChildCourseDetails> getAll(String childId) {
		ChildCourseDetails details = new ChildCourseDetails();
		details.setChildId(childId);

		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(childId));

        DynamoDBQueryExpression<ChildCourseDetails> expr = new DynamoDBQueryExpression<ChildCourseDetails>()
            .withKeyConditionExpression("childId = :val1")
            .withExpressionAttributeValues(eav);
		return mapper.query(ChildCourseDetails.class, expr);
	}
}
