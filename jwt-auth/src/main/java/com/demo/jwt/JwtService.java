package com.demo.jwt;

import com.demo.jwt.util.JwtProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * com.demo.jwt
 *
 * @author : idasom
 */
@Service
@Transactional(readOnly = true)
public class JwtService {

    private JwtProvider jwtProvider;

    public JwtService(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public String login(String username) {
        // db에서 username 으로 조회
        // 없으면 예외처리

        // 토큰 생성
        return jwtProvider.generateToken(username);
    }
}
