package com.teamechoes.echoesoftheconvergence.dao;

import com.teamechoes.echoesoftheconvergence.interfaces.DAOBase;
import com.teamechoes.echoesoftheconvergence.interfaces.DynamoDBContainer;
import com.teamechoes.echoesoftheconvergence.objects.Ship;

public class ShipDAO extends DAOBase{
	public static Ship get(String id){
		return DynamoDBContainer.getMapper().load(Ship.class, id);
	}
}
