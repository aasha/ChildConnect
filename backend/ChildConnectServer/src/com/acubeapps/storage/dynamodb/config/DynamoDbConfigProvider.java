package com.acubeapps.storage.dynamodb.config;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class DynamoDbConfigProvider {

    private static final AmazonDynamoDB ddbClient = new AmazonDynamoDBClient(new ProfileCredentialsProvider());

	public static AmazonDynamoDB getDynamoDb() {
		ddbClient.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
		return ddbClient;
	}
}
