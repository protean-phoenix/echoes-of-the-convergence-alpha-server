package com.teamechoes.echoesoftheconvergenceserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketStarter extends Thread {

	ServerSocket ss;
	
	public SocketStarter(ServerSocket ss) {
		this.ss = ss;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		Socket s = null;
		System.out.println("listening...");
		try {
			s=ss.accept();//establishes connection
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Connection established!");
		try {
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
