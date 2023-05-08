package com.teamechoes.accountmicroservice;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.dynamodbv2.model.DuplicateItemException;
import com.teamechoes.echoesoftheconvergence.objects.Account;

@RestController
public class AccountController {
	
	@Autowired
	AccountService service;
	
	@PutMapping(path = "/register", 
	        consumes = MediaType.APPLICATION_JSON_VALUE, 
	        produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> registerAccount(@RequestBody String data) {
		//{
		//  "email":[text]
		//	"username":[text]
		//	"password":[text]
		//}
		JSONObject jo = new JSONObject(data);
		Account acc = new Account();
		acc.setEmail(jo.getString("email"));
		acc.setUsername(jo.getString("username"));
		acc.setPassword(jo.getString("password"));
		try {
			service.registerAccount(acc);
			return new ResponseEntity<String>("Account registration successful!", HttpStatus.CREATED);
		}catch(DuplicateItemException die) {
			return new ResponseEntity<String>("There is already an account with this username", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(path = "/login", 
	        consumes = MediaType.APPLICATION_JSON_VALUE, 
	        produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> userLogin(@RequestBody String data) {
		//{
		//  "handle":[text]
		//	"password":[text]
		//}
		JSONObject jo = new JSONObject(data);

		Account acc;
		acc = service.userLogin(jo);
		if(acc == null) {
			return new ResponseEntity<String>("Username/email or password was incorrect", HttpStatus.BAD_REQUEST);
		}else {
			JSONObject res = new JSONObject();
			res.put("username", acc.getUsername());
			res.put("token", service.createJWT(acc));
			return new ResponseEntity<String>(res.toString(), HttpStatus.OK);
		}
	}
	
	@PostMapping(path = "/validate",
		consumes = MediaType.TEXT_PLAIN_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> validateToken(@RequestBody String token){
		String out = service.decodeJWT(token).toString();
		return new ResponseEntity<String>(out, HttpStatus.OK);
	}
}
