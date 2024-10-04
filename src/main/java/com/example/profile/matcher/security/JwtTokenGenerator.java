package com.example.profile.matcher.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenGenerator {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(String username) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration * 1000);

        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

}
