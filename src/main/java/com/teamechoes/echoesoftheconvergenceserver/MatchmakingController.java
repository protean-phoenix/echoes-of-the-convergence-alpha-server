package com.teamechoes.echoesoftheconvergenceserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

		SocketThread sstart = new SocketThread(ss, 1);
		sstart.start();

		String id = MatchmakingHost.createLobby(ip_string, ss.getLocalPort(), sstart);

		JSONObject jo = new JSONObject();
		jo.put("port", ss.getLocalPort());
		jo.put("id", id);
		jo.put("player_id", 0);

		return new ResponseEntity<String>(jo.toString(), HttpStatus.OK);
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

	@GetMapping(path = "/join-lobby/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> joinLobby(HttpServletRequest request, @PathVariable("id") String id){
		ArrayList<Lobby> lobbies = MatchmakingHost.getLobbies();
		Lobby target = null;
		for(Lobby l: lobbies) {
			if(id.equals(l.getId())){
				target = l;
			}
		}
		if(target == null) {
			return new ResponseEntity<String>("No such lobby!", HttpStatus.BAD_REQUEST);
		}

		String ip_string = request.getRemoteAddr();
		target.setPlayer2IP(ip_string);

		ServerSocket ss = null;
		try {
			ss = new ServerSocket(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SocketThread sstart = new SocketThread(ss, 2);
		target.setPlayer2Thread(sstart);
		target.setPlayer2port(ss.getLocalPort());
		sstart.start();

		JSONObject jo = new JSONObject();
		jo.put("port", ss.getLocalPort());
		jo.put("id", target.getId());
		jo.put("player_id", 1);

		return new ResponseEntity<String>(jo.toString(), HttpStatus.OK);
	}

	@GetMapping(path = "/get-primed", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> getPrimed(){
		if(MatchmakingHost.primed.size() > 0) {
			Lobby l = MatchmakingHost.primed.remove(0);
			JSONObject jo = new JSONObject();
			jo.put("player1", l.player1IP);
			jo.put("player2", l.player2IP);
			jo.put("port1", l.player1port);
			jo.put("port2", l.player2port);
			return new ResponseEntity<String>(jo.toString(), HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("No lobbies available!", HttpStatus.BAD_REQUEST);
		}
	}
}
