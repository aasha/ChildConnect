package com.acubeapps.storage.dynamodb.dao;

import java.util.List;

import com.acubeapps.storage.dynamodb.config.DynamoDbConfigProvider;
import com.acubeapps.storage.dynamodb.pojo.CourseDetailsDdb;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

public class CourseDetailsDao {
    private DynamoDBMapper mapper;

    public CourseDetailsDao() {
        mapper = new DynamoDBMapper(DynamoDbConfigProvider.getDynamoDb());
    }

    public void save(CourseDetailsDdb pojo) {
        mapper.save(pojo);
    }

    public CourseDetailsDdb get(String courseId) {
        CourseDetailsDdb details = new CourseDetailsDdb();
        details.setCourseId(courseId);
        return mapper.load(details);
    }

    public List<CourseDetailsDdb> getAll() {
    	return mapper.scan(CourseDetailsDdb.class, new DynamoDBScanExpression());
    }
}
