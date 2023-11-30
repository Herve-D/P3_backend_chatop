package com.openclassrooms.chatop.configuration;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import static java.lang.String.format;

@Service
public class JwtService {

	private final String jwtKey = "8G3J2By8NiKaskU/FJ0YulfunlvhsbkHr/X7u7hq8LQ";
	private final String jwtIssuer = "fr.herve";

	private final Logger logger = LoggerFactory.getLogger(JwtService.class);

	public String generateToken(User user) {
		return Jwts.builder()
				.setSubject(format("%s", user.getUsername()))
				.setIssuer(jwtIssuer).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
				.signWith(SignatureAlgorithm.HS256, jwtKey)
				.compact();
	}

	public String getUsername(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token).getBody();
		return claims.getSubject().split(",")[0];
	}

	public Date getExpirationDate(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token).getBody();
		return claims.getExpiration();
	}

	public boolean validate(String token) {
		try {
			Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token);
			return true;
		} catch (SignatureException ex) {
			logger.error("Invalid JWT signature - {}", ex.getMessage());
		} catch (MalformedJwtException ex) {
			logger.error("Invalid JWT token - {}", ex.getMessage());
		} catch (ExpiredJwtException ex) {
			logger.error("Expired JWT token - {}", ex.getMessage());
		} catch (UnsupportedJwtException ex) {
			logger.error("Unsupported JWT token - {}", ex.getMessage());
		} catch (IllegalArgumentException ex) {
			logger.error("JWT claims string is empty - {}", ex.getMessage());
		}
		return false;
	}

}
