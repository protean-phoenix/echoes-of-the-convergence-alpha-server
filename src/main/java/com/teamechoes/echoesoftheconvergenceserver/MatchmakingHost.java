package com.teamechoes.echoesoftheconvergenceserver;

import java.util.ArrayList;
import java.util.Collections;

public class MatchmakingHost {
	public static ArrayList<Lobby> lobbies;
	
	public static void createLobby(String ip, int port) {
		if(lobbies == null) lobbies = new ArrayList<Lobby>();
		lobbies.add(new Lobby(ip, port));
	}
	
	public static void removeLobby(String id) {
		for(Lobby l: lobbies) {
			if(l.getId().equals(id)) {
				lobbies.remove(l);
			}
		}
	}
	
	public static ArrayList<Lobby> getLobbies(){
		return lobbies;
	}
}
