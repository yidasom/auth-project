package com.demo.jwt.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * com.demo.jwt.util
 *
 * @author : idasom
 */
@RestController
@RequestMapping("/auth")
public class JwtController {

    private JwtProvider jwtProvider;

    public JwtController(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    /**
     * 로그인 후 JWT 토큰 발급
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username) {
        String token = jwtProvider.generateToken(username);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    /**
     * 토큰 유효성 검증
     */
    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        boolean isValid = jwtProvider.validateToken(token);
        return new ResponseEntity<>(isValid, HttpStatus.OK);
    }

    /**
     * 토큰에서 사용자 정보 추출
     */
    @GetMapping("/user")
    public ResponseEntity<?> getUserFromToken(@RequestParam String token) {
        String user = jwtProvider.getUsername(token);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
