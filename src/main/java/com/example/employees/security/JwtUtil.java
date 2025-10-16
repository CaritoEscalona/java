package com.example.employees.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtUtil {
  @Value("${security.jwt.secret}") private String secret;
  @Value("${security.jwt.exp}") private long expMillis;

  public String generateToken(UserDetails user, long expMillis) {
	    Date now = new Date();
	    Date exp = new Date(now.getTime() + expMillis);
	    Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

	    return Jwts.builder()
	    	    .subject(user.getUsername())
	    	    .claim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
	    	    .issuedAt(now)
	    	    .expiration(exp)
	    	    .signWith(key)
	    	    .compact();

	}


}
