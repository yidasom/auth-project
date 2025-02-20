package com.demo.jwt.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

/**
 * com.demo.jwt.util
 *
 * @author : idasom
 */
public class JwtProvider {
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${jwt.access-token.expiration-time}")
    private long EXPIRATION_TIME;

    private SecretKey getSigningKey() {
        byte[] key = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(key);
    }

    private JwtBlacklistService jwtBlacklistService;

    public JwtProvider(JwtBlacklistService jwtBlacklistService) {
        this.jwtBlacklistService = jwtBlacklistService;
    }

    // 토큰 생성
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SIG.HS256)
                .compact();
    }

    // 토큰 validation
    public Boolean validateToken(String token) {
        if (jwtBlacklistService.isBlacklisted(token)) {
            return false;
        }
        try {
            JwtParser parser = Jwts.parser().verifyWith(getSigningKey()).build();

            Jwt<?, ?> jwt = parser.parse(token);
            // Claim : 사용자정보, 만료시간
            if (jwt instanceof Jws<?> jws && jws.getPayload() instanceof Claims) {
                return true;
            } else {
                return false;
            }
        } catch (JwtException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 사용자 정보
    public String getUsername(String token) {
        try {
            JwtParser parser = Jwts.parser().verifyWith(getSigningKey()).build();

            Jwt<?, ?> jwt = parser.parse(token);
            if (jwt instanceof Jws<?> jws && jws.getPayload() instanceof Claims claims) {
                return claims.getSubject();
            }
        } catch (JwtException e) {
            e.printStackTrace();
        }
        return null;
    }

    // jwt 파싱
    private Claims parseClaims(String token) {
        try {
            JwtParser parser = Jwts.parser().verifyWith(getSigningKey()).build();

            Jws<Claims> jws = parser.parseSignedClaims(token);
            return jws.getPayload();
        } catch (JwtException e) {
            e.printStackTrace();
            return null;
        }
    }
}
