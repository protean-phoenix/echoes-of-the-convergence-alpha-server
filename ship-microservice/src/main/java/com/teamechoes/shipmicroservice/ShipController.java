package com.teamechoes.shipmicroservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamechoes.echoesoftheconvergence.dao.AccountDAO;
import com.teamechoes.echoesoftheconvergence.objects.Account;

@RestController
public class ShipController {
	@Autowired
	ShipService service;
	
	@PostMapping(path = "/new", 
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createNewShip(@RequestHeader(value = "user-id", required = true) String user_id) {
		Account acc = AccountDAO.getById(user_id);
		System.out.println("new ship invoked by " + acc.getUsername());
		return null;
	}
	
}
