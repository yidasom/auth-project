package com.demo.jwt.util;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;

/**
 * com.demo.jwt.util
 *
 * @author : idasom
 */
@Component
public class JwtProvider {
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${jwt.access-token.expiration-time}")
    private long EXPIRATION_TIME;

    private SecretKey getSigningKey() {
        byte[] key = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(key);
    }


    // 토큰 생성
    public String generateToken(String username) {
        return null;
    }

    // 토큰 validation
    public Boolean validateToken(String token) {
        return null;
    }

    // 사용자 정보
    public String getUsername(String token) {
        return null;
    }
}
