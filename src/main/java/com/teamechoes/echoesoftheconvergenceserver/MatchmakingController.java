package com.teamechoes.echoesoftheconvergenceserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.ArrayList;

import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class MatchmakingController {
	@GetMapping(path = "/open-lobby", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> printIP(HttpServletRequest request){
		String ip_string = request.getRemoteAddr();
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SocketStarter sstart = new SocketStarter(ss);
		sstart.start();

		MatchmakingHost.createLobby(ip_string, ss.getLocalPort());
		
		return new ResponseEntity<String>("" + ss.getLocalPort(), HttpStatus.OK);
	}
	
	@GetMapping(path = "/lobbies", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getLobbies(){
		ArrayList<Lobby> lobbies = MatchmakingHost.getLobbies();
		JSONArray ja = new JSONArray();
		for(Lobby l: lobbies) {
			ja.put(l.getId());
		}
		
		return new ResponseEntity<String>(ja.toString(), HttpStatus.OK);
	}
}
