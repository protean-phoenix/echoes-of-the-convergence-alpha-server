package com.teamechoes.echoesoftheconvergence;

import java.util.Iterator;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.teamechoes.echoesoftheconvergence.interfaces.DynamoDBContainer;
import com.teamechoes.echoesoftheconvergence.objects.Account;

public class TestClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        
		deleteAllTables();
	}

	public static void deleteAllTables() {
		Iterator iter = DynamoDBContainer.getDynamo().listTables().iterator();
		while(iter.hasNext()) {
			Table table = (Table) iter.next();
			DynamoDBContainer.getClient().deleteTable(table.getTableName());
		}
	}
	
	public static List<Account> getAllAccounts(){
		// Change to your Table_Name (you can load dynamically from lambda env as well)
		DynamoDBMapperConfig mapperConfig = new DynamoDBMapperConfig.Builder().withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement("accounts")).build();

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

		// Change to your model class   
		List<Account> scanResult = DynamoDBContainer.getMapper().scan(Account.class, scanExpression);
		
		return scanResult;
	}
	
	public static void deleteAllAccounts() {
		List<Account> all = getAllAccounts();
		for(Account m : all) {
			DynamoDBContainer.getMapper().delete(m);
		}
	}
	
}
