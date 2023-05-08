package com.teamechoes.matchmakingmicroservice;

import java.util.ArrayList;
import java.util.Collections;

public class MatchmakingHost {
	public static ArrayList<Lobby> lobbies;
	public static ArrayList<Lobby> primed;
	
	public static String createLobby(String ip, int port, SocketThread thread) {
		if(lobbies == null) lobbies = new ArrayList<Lobby>();
		Lobby new_lobby = new Lobby(ip, port, thread);
		lobbies.add(new_lobby);
		return new_lobby.getId();
	}
	
	public static void removeLobby(String id) {
		for(int i = 0; i < lobbies.size(); i++){
			if(lobbies.get(i).getId().equals(id)) {
				lobbies.remove(i);
				break;
			}
		}
	}
	
	public static ArrayList<Lobby> getLobbies(){
		if(lobbies == null) lobbies = new ArrayList<Lobby>();
		return lobbies;
	}
	
	public static Lobby getLobbyByPlayer1Thread(SocketThread st) {
		for(Lobby l: lobbies) {
			if(l.getPlayer1Thread() == st) {
				return l;
			}
		}
		return null;
	}
	
	public static Lobby getLobbyByPlayer2Thread(SocketThread st) {
		for(Lobby l: lobbies) {
			if(l.getPlayer2Thread() == st) {
				return l;
			}
		}
		return null;
	}
	
	public static void moveLobbyToPrimed(Lobby l) {
		lobbies.remove(l);
		if(primed == null) primed = new ArrayList<Lobby>();
		primed.add(l);
	}
}
