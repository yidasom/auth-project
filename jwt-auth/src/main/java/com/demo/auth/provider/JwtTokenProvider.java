package com.demo.auth.provider;

import io.jsonwebtoken.Jwts;

import java.util.Date;

public class JwtTokenProvider {
    public String generateToken(String username, long expSeconds) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(expSeconds))
//                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
}
