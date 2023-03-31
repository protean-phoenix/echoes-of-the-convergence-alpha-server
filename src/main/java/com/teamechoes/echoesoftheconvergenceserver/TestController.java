package com.teamechoes.echoesoftheconvergenceserver;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class TestController {
	@GetMapping(path = "/print-ip", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> printIP(HttpServletRequest request){
		String ip_string = request.getRemoteAddr();
		
		return new ResponseEntity<String>(ip_string, HttpStatus.OK);
	}
	
}
