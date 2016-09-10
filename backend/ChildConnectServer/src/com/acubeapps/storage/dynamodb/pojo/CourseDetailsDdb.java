package com.acubeapps.storage.dynamodb.pojo;

import com.acubeapps.service.pojo.CourseDetails;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "CourseDetailsDdb")
public class CourseDetailsDdb {
	private String courseId;
	private CourseDetails courseDetails;

	@DynamoDBHashKey(attributeName = "courseId")
	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public CourseDetails getCourseDetails() {
		return courseDetails;
	}

	public void setCourseDetails(CourseDetails courseDetails) {
		this.courseDetails = courseDetails;
	}
}
