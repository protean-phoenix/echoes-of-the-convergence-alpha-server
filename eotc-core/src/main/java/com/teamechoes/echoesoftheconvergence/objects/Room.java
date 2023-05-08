package com.teamechoes.echoesoftheconvergence.objects;

import org.json.JSONObject;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.teamechoes.echoesoftheconvergence.interfaces.DynamoObject;

import lombok.Data;

@Data
@DynamoDBTable(tableName="rooms")
public class Room extends DynamoObject {
	@DynamoDBHashKey(attributeName = "id")
    @DynamoDBAutoGeneratedKey
	String id;
	
	int type;
	float x, y;
	
	public Room() {
		
	}

	public Room(JSONObject jo) {
		super(jo);
	}
	
}