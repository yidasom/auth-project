package com.demo.jwt.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * com.demo.jwt
 *
 * @author : idasom
 */
public interface JwtService extends UserDetailsService {

    void updateAfterUserLoginSuccess(long userSeq, String refreshToken, HttpServletRequest request) throws Exception;

//    private JwtProvider jwtProvider;
//
//    public JwtService(JwtProvider jwtProvider) {
//        this.jwtProvider = jwtProvider;
//    }
//
//    @Transactional
//    public String login(UserDTO userDTO) {
//        // db에서 username 으로 조회
//        // 없으면 예외처리
//
//        // 토큰 생성
//        return jwtProvider.generateToken(username);

}
