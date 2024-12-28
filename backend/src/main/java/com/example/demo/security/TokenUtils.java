package com.example.demo.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenUtils {

	@Value("SBNZ-APP")
	private String APP_NAME;
	
	@Value("SBNZ-SECRET")
	private String APP_SECRET;
			
	@Value("1000000000")
	private long EXPIRES_IN;
	
	private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
	
	public String generateToken(String email) {
		return Jwts.builder()
				.setSubject(email)
				.setIssuer(this.APP_NAME)
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + this.EXPIRES_IN))
				.signWith(this.SIGNATURE_ALGORITHM, this.APP_SECRET).compact();
	}
	
	public boolean validateToken(UserDetails user, String token) {
		String email = this.getEmail(token);
		return email != null && email.equals(user.getUsername());
	}
		
	public String getEmail(String token) {
		return Jwts.parser()
				.setSigningKey(this.APP_SECRET)
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}

}
