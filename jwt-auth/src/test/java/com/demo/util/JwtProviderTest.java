package com.demo.util;

import com.sample.jwt.util.JwtProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * util
 *
 * @author : idasom
 * @data : 2/15/25
 */
@SpringBootTest
@TestPropertySource(properties = {
        "jwt.secret-key=my-super-secret-key-which-should-be-long-enough",
        "jwt.access-token.expiration-time=3600000"
})
public class JwtProviderTest {

    @Autowired
    private JwtProvider jwtProvider;

    @Test
    void 토큰_검증() {
        String token = jwtProvider.generateToken("test");
        assertNotNull(token);

        boolean isValid = jwtProvider.validateToken(token);
        assertTrue(isValid);

        String username = jwtProvider.getUsername(token);
        assertEquals("test", username);  // 수정된 부분
    }

    @Test
    void 만료된_토큰_검증_실패() {
        String token = jwtProvider.generateToken("expiredUser"); // 즉시 만료
        boolean isValid = jwtProvider.validateToken(token);
        assertFalse(isValid);
    }
}
