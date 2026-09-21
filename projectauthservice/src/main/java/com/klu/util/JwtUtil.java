package com.klu.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final String SECRET_KEY =
            "PS027EnterpriseIssueEscalationSecretKey2026ForJWTAuthentication";

    private final Key key = Keys.hmacShaKeyFor(
            SECRET_KEY.getBytes(StandardCharsets.UTF_8)
    );

    public String generateToken(
            Long id,
            String username,
            String role) {

        return Jwts.builder()
                .claim("id", id)
                .claim("username", username)
                .claim("role", role)
                .subject(username)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                + 1000L * 60 * 60 * 10
                        )
                )
                .signWith(key)
                .compact();
    }

    public Claims validateToken(String token) {

        return Jwts.parser()
                .verifyWith(
                        (javax.crypto.SecretKey) key
                )
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {

        return validateToken(token).getSubject();
    }
}