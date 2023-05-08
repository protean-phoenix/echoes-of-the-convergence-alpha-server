package com.teamechoes.echoesoftheconvergence.interfaces;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

public class DynamoDBContainer {
	static AmazonDynamoDB client;
	static DynamoDB dynamo;
	static DynamoDBMapper mapper;
    private static String awsAccessKey = "XXXXX";
    private static String awsSecretKey = "XXXXX";


	private static void initDynamoDB() {
		
		//Local Testing
		client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
				new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-east-1"))
				.withCredentials(new AWSStaticCredentialsProvider(
	                    (AWSCredentials) new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
				.build();
		
		/*
		//Production
		client = AmazonDynamoDBClientBuilder.standard()
	            .withCredentials(new AWSStaticCredentialsProvider(
	                    (AWSCredentials) new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
	            .withRegion(Regions.US_EAST_1).build();
		*/
		
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
