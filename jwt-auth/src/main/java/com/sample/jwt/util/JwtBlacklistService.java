package com.sample.jwt.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * com.demo.jwt.util
 *
 * @author : idasom
 */
@Service
public class JwtBlacklistService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 블랙리스트에 토큰 추가 (로그아웃 시 호출)
    public void addToBlacklist(String token) {
        redisTemplate.opsForValue().set(token, "blacklisted", Duration.ofMinutes(60));
    }

    // 토큰이 블랙리스트에 있는지 확인
    public boolean isBlacklisted(String token) {
        return redisTemplate.hasKey(token);
    }
}
