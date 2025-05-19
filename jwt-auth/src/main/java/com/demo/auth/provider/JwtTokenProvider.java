package com.demo.auth.provider;

import com.demo.member.dto.MemberDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider implements InitializingBean {

    @Value("${jwt.secret-key}")
    private String secret;

    private Key key;

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication authentication, MemberDTO memberDTO, Date expSeconds, String tokenType) {

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("tokenType", tokenType)
                .claim("uuid", memberDTO.getUuid())
                .claim("username", memberDTO.getUsername())
                .claim("accessExp", memberDTO.getAccessExp())
                .claim("refreshExp", memberDTO.getRefreshExp())
                .setExpiration(expSeconds)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
