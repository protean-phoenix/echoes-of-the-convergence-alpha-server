package com.teamechoes.echoesoftheconvergenceserver;

import java.util.UUID;

import lombok.Data;

@Data
public class Lobby {
	int player1port, player2port;
	String id, player1IP, player2IP;
	SocketThread player1Thread, player2Thread;
	
	public Lobby(String player1IP, int player1port, SocketThread player1Thread) {
		id = UUID.randomUUID().toString();
		this.player1IP = player1IP;
		this.player1port = player1port;
		this.player1Thread = player1Thread;
	}
	
	public void broadcast(String s) {
		player1Thread.sendMessage(s);
		player2Thread.sendMessage(s);
	}
	
}