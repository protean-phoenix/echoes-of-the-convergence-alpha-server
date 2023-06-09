package com.cavegaming.tournamastercore.interfaces;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

public class DynamoDBContainer {
	static AmazonDynamoDB client;
	static DynamoDB dynamo;
	static DynamoDBMapper mapper;
    private static String awsAccessKey = "AKIA6B7VVUSKIQWDUNU3";
    private static String awsSecretKey = "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";


	private static void initDynamoDB() {
		/*
		//Local Testing
		client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
				new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-east-1"))
				.build();
		*/
		
		//Production
		client = AmazonDynamoDBClientBuilder.standard()
	            .withCredentials(new AWSStaticCredentialsProvider(
	                    (AWSCredentials) new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
	            .withRegion(Regions.US_EAST_1).build();
		
		
		dynamo = new DynamoDB(client);
		mapper = new DynamoDBMapper(client);
	}
	
	public static AmazonDynamoDB getClient() {
		if(client == null) {
			initDynamoDB();
		}
		return client;
	}
	
	
	public static DynamoDB getDynamo() {
		if(dynamo == null) {
			initDynamoDB();
		}
		return dynamo;
	}
	
	public static DynamoDBMapper getMapper() {
		if(mapper == null) {
			initDynamoDB();
		}
		return mapper;
	}
}
