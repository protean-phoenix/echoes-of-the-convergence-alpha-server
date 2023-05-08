package com.teamechoes.echoesoftheconvergence.dao;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DuplicateItemException;
import com.teamechoes.echoesoftheconvergence.interfaces.DAOBase;
import com.teamechoes.echoesoftheconvergence.interfaces.DynamoDBContainer;
import com.teamechoes.echoesoftheconvergence.objects.Account;


public class AccountDAO extends DAOBase{
	
	public static void insertNew(Account acc) {
		if(getByUsername(acc.getUsername()) == null) {
			acc.setCreation(LocalDateTime.now(ZoneId.of("UTC")));
			DynamoDBContainer.getMapper().save(acc);
		}else {
			throw new DuplicateItemException("An account already exists with that username");
		}
	}
	
	public static Account get(String id, String username){
		return DynamoDBContainer.getMapper().load(Account.class, id, username);
	}
	
	public static Account getById(String id){
		Map<String, AttributeValue> values = new HashMap<String, AttributeValue>();
		values.put(":id", new AttributeValue().withS(id));
		
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
		    .withFilterExpression("id = :id")
		    .withExpressionAttributeValues(values);
		
		List<Account> scanResults = DynamoDBContainer.getMapper().scan(Account.class, scanExpression);
		
		if(scanResults.size() > 0) return scanResults.get(0);
		else return null;
	}
	
	public static Account getByEmail(String email){
		Map<String, AttributeValue> values = new HashMap<String, AttributeValue>();
		values.put(":email", new AttributeValue().withS(email));
		
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
		    .withFilterExpression("email = :email")
		    .withExpressionAttributeValues(values);
		
		List<Account> scanResults = DynamoDBContainer.getMapper().scan(Account.class, scanExpression);
		
		if(scanResults.size() > 0) return scanResults.get(0);
		else return null;
	}
	
	public static Account getByUsername(String username){
		Map<String, AttributeValue> values = new HashMap<String, AttributeValue>();
		values.put(":username", new AttributeValue().withS(username));
		
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
		    .withFilterExpression("username = :username")
		    .withExpressionAttributeValues(values);
		
		List<Account> scanResults = DynamoDBContainer.getMapper().scan(Account.class, scanExpression);
		
		if(scanResults.size() > 0) return scanResults.get(0);
		else return null;
	}
}
