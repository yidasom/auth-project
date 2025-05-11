package com.demo.auth.provider;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * com.demo.auth.provider
 *
 * @author : idasom
 * @data : 5/12/25
 */
@Component
public class JwtProviderRedis {
    public String generateToken(String username, long expSeconds) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(expSeconds))
//                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

}
