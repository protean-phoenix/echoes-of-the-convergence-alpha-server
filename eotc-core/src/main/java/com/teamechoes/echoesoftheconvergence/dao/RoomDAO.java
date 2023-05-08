package com.teamechoes.echoesoftheconvergence.dao;

import java.time.LocalDateTime;
import java.time.ZoneId;

import com.amazonaws.services.dynamodbv2.model.DuplicateItemException;
import com.teamechoes.echoesoftheconvergence.interfaces.DAOBase;
import com.teamechoes.echoesoftheconvergence.interfaces.DynamoDBContainer;
import com.teamechoes.echoesoftheconvergence.objects.Account;
import com.teamechoes.echoesoftheconvergence.objects.Room;

public class RoomDAO extends DAOBase {
	public static Room get(String id){
		return DynamoDBContainer.getMapper().load(Room.class, id);
	}
}
