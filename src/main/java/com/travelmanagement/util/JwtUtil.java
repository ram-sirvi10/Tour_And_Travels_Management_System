package com.travelmanagement.util;

import java.io.InputStream;
import java.security.Key;
import java.util.Date;
import java.util.Properties;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {

	private static final Key SECRET_KEY;
	private static final long EXPIRATION_MS;

	static {
		Properties props = new Properties();
		try (InputStream is = JwtUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
			props.load(is);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load config.properties", e);
		}

		String secretString = props.getProperty("jwt.secret");
		if (secretString == null || secretString.length() < 32) {
			throw new IllegalArgumentException("JWT secret key is missing or too short!");
		}

		SECRET_KEY = Keys.hmacShaKeyFor(secretString.getBytes());
		EXPIRATION_MS = Long.parseLong(props.getProperty("jwt.expirationMs"));
	}

	public static String generateToken(String userId, String role) {
		return Jwts.builder().setSubject(userId).claim("role", role).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
				.signWith(SECRET_KEY, SignatureAlgorithm.HS256).compact();
	}

	public static Claims parseToken(String token) throws JwtException {
		return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
	}
}
