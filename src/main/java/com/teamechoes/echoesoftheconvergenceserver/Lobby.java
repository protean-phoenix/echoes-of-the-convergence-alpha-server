package com.teamechoes.echoesoftheconvergenceserver;

import java.util.UUID;

import lombok.Data;

@Data
public class Lobby {
	int port;
	String id, player1IP, player2IP;
	
	public Lobby(String player1IP, int port) {
		id = UUID.randomUUID().toString();
		this.player1IP = player1IP;
		this.port = port;
	}
}