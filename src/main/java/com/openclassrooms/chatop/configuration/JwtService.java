package com.openclassrooms.chatop.configuration;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${jwt.secret.key}")
	private String jwtSecretKey;

	private final String jwtIssuer = "fr.herve";

	private final Logger logger = LoggerFactory.getLogger(JwtService.class);

	/**
	 * Generate an access token for the giver user.
	 * 
	 * @param user - The user for whom to generate the token.
	 * @return The generated access token.
	 */
	public String generateToken(User user) {
		return Jwts.builder()
				.setSubject(format("%s", user.getUsername()))
				.setIssuer(jwtIssuer)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 1 week
				.signWith(SignatureAlgorithm.HS256, jwtSecretKey)
				.compact();
	}

	/**
	 * Get the username from the JWT token.
	 * 
	 * @param token - The JWT token.
	 * @return THe username extracted from the token.
	 */
	public String getUsername(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
		return claims.getSubject().split(",")[0];
	}

	/**
	 * Get the expiration date from the JWT token.
	 * 
	 * @param token - The JWT token.
	 * @return The expiration date extracted from the token.
	 */
	public Date getExpirationDate(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
		return claims.getExpiration();
	}

	/**
	 * Validate the JWT token.
	 * 
	 * @param token - The JWT token to validate.
	 * @return True if the token is valid, false otherwise.
	 */
	public boolean validate(String token) {
		try {
			Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token);
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
