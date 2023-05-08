package com.teamechoes.accountmicroservice;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.regex.Pattern;

import javax.crypto.spec.SecretKeySpec;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.teamechoes.echoesoftheconvergence.dao.AccountDAO;
import com.teamechoes.echoesoftheconvergence.objects.Account;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.xml.bind.DatatypeConverter;

@Service
public class AccountService {
	//used to configure the JWT encryption and decryption
	static String secret_key = "ghwVPt35XTXUXB8a83MU";
	
	public void registerAccount(Account acc) { //encrypts the password before submitting the account into the database
		acc.setPassword(encrypt(acc.getPassword()));
		AccountDAO.insertNew(acc);
	}
	
	public Account userLogin(JSONObject jo) { //verifies login by checking the raw string against the encrypted password	
		Account acc = new Account();
		
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
                  
		Pattern pat = Pattern.compile(emailRegex);
		String handle = jo.getString("handle");
		if(pat.matcher(handle).matches()) {
			acc= AccountDAO.getByEmail(jo.getString("handle"));
		}else {
			acc= AccountDAO.getByUsername(jo.getString("handle"));
		}

		if(encrypt(jo.getString("password")).equals(acc.getPassword())) {
			return acc;
		}else {
			return null;
		}
	}
	
	public String encrypt(String input) {
		String output = "0";
		try {
			output = toHexString(getSHA(input));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
	}
	
    public byte[] getSHA(String input) throws NoSuchAlgorithmException  
    {  
        /* MessageDigest instance for hashing using SHA256 */  
        MessageDigest md = MessageDigest.getInstance("SHA-256");  
  
        /* digest() method called to calculate message digest of an input and return array of byte */  
        return md.digest(input.getBytes(StandardCharsets.UTF_8));  
    }  
      
    public String toHexString(byte[] hash)  
    {  
        /* Convert byte array of hash into digest */  
        BigInteger number = new BigInteger(1, hash);  
  
        /* Convert the digest into hex value */  
        StringBuilder hexString = new StringBuilder(number.toString(16));  
  
        /* Pad with leading zeros */  
        while (hexString.length() < 32)  
        {  
            hexString.insert(0, '0');  
        }  
  
        return hexString.toString();  
    }  
	
	//creates a JWT token that lasts from now until ttlMillis milliseconds
	public String createJWT(Account acc) {
		  
	    long nowMillis = System.currentTimeMillis();
	    Date now = new Date(nowMillis);
	  
	    
	    String token = null;
		try {
		    Algorithm algorithm = Algorithm.HMAC256(secret_key);
		    token = JWT.create()
		    	.withSubject(acc.getId())
		    	.withIssuedAt(now)
		    	.withIssuer("team-echoes")
		        .sign(algorithm);
		} catch (JWTCreationException exception){
		    exception.printStackTrace();
		}
	    //Builds the JWT and serializes it to a compact, URL-safe string
	    return token;
	}
	
	public JSONObject decodeJWT(String jwt) {
	    //This line will throw an exception if it is not a signed JWS (as expected)
		
		DecodedJWT decodedJWT = null;
		try {
		    Algorithm algorithm =  Algorithm.HMAC256(secret_key);
		    JWTVerifier verifier = JWT.require(algorithm)
		        // specify an specific claim validations
		        .withIssuer("team-echoes")
		        // reusable verifier instance
		        .build();
		        
		    decodedJWT = verifier.verify(jwt);
		} catch (JWTVerificationException exception){
		    // Invalid signature/claims
			exception.printStackTrace();
		}
		
		String data = decodedJWT.getClaims().toString().replace('=', ':');
		JSONObject jo = new JSONObject(data);
	    return jo;
	}
}
