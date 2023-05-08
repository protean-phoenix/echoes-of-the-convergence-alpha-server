package com.teamechoes.echoesoftheconvergence.interfaces;

//right now this superclass is pointless
public abstract class DAOBase {	
	public <T extends DynamoObject> void save(T obj){
		DynamoDBContainer.getMapper().save(obj);
	}
	
	public <T extends DynamoObject> void delete(T obj) {
		DynamoDBContainer.getMapper().delete(obj);
	}
}
