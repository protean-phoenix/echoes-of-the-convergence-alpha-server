package com.teamechoes.shipmicroservice;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.teamechoes.echoesoftheconvergence.dao.RoomDAO;
import com.teamechoes.echoesoftheconvergence.dao.ShipDAO;
import com.teamechoes.echoesoftheconvergence.objects.Room;
import com.teamechoes.echoesoftheconvergence.objects.Ship;

@Service
public class ShipService {

	public JSONObject getShip(String id) {
		Ship s = ShipDAO.get(id);
		JSONArray room_array = new JSONArray();
		Set<String> room_ids = s.getRooms();
		if(room_ids != null) {
			for(String room_id: room_ids) {
				Room r = RoomDAO.get(room_id);
				JSONObject r_jo = r.toJSONObject();
			}
		}
		
		return null;
	}
}
