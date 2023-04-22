package com.teamechoes.echoesoftheconvergenceserver;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

public class SocketThread extends Thread {

	ServerSocket ss = null;
	Socket s = null;
	int playerId;
	private DataInputStream in = null;
	private DataOutputStream out = null;
	
	public SocketThread(ServerSocket ss, int playerId) {
		this.ss = ss;
		this.playerId = playerId;
	}

	public void run() {
		// TODO Auto-generated method stub
		System.out.println("listening...");
		try {
			s=ss.accept();//establishes connection
			System.out.println("Connection established!");
			in = new DataInputStream(new BufferedInputStream(s.getInputStream()));
			out = new DataOutputStream(s.getOutputStream());

			if(playerId == 2) {
				Lobby rm = MatchmakingHost.getLobbyByPlayer2Thread(this);
				MatchmakingHost.moveLobbyToPrimed(rm);
				rm.broadcast("Match ready!");
				rm.player1Thread.closeConnection();
				rm.player2Thread.closeConnection();
				
				/*
				String[] cmdarray = new String[1]; //the size of this string array must EXACTLY EQUAL the number of commands you're feeding to the runtime
				//cmdarray[0] = "C:/Users/Ace/Documents/EoTC/lobby/echoes-of-the-convergence-alpha-lobby.exe"; //testing
				cmdarray[0] = "lobby-dedicated/echoes-of-the-convergence-lobby.x86_64"; //production		
				//cmdarray[1] = "-batchmode";
				
				try {
					Runtime.getRuntime().exec(cmdarray);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
				return;
			}
			
			String line = "";

			try
			{
				while (!line.equals("EOF"))
				{
					line = in.readUTF();
					System.out.println(line);
				}
			}
			catch(SocketException i)
			{
				System.out.println("detected socket termination");
				if(playerId == 1) {
					Lobby rm = MatchmakingHost.getLobbyByPlayer1Thread(this);
					if(rm != null) 	MatchmakingHost.removeLobby(rm.getId());
					System.out.println("lobby terminated");
				}else {
					Lobby rm = MatchmakingHost.getLobbyByPlayer2Thread(this);
					rm.setPlayer2IP(null);
					rm.setPlayer2Thread(null);
				}
			}
			catch(EOFException i)
			{
				System.out.println("detected socket termination");
				if(playerId == 1) {
					Lobby rm = MatchmakingHost.getLobbyByPlayer1Thread(this);
					if(rm != null)	MatchmakingHost.removeLobby(rm.getId());
					System.out.println("lobby terminated");
				}else {
					Lobby rm = MatchmakingHost.getLobbyByPlayer2Thread(this);
					rm.setPlayer2IP(null);
					rm.setPlayer2Thread(null);
				}
			}
			catch(IOException i)
			{
				System.out.println(i);
			}

			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendMessage(String s) {
		try {
			out.writeUTF(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void closeConnection() {
		try {
			s.close();
			ss.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
