package com.demo.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * com.demo.user
 *
 * @author : idasom
 */
@Getter
@Setter
@NoArgsConstructor
public class SnsDTO {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final String state = new BigInteger(130, RANDOM).toString();

    private final String responseType = "code";
    private String scope = "email profile openid";
    private String code = "";

    //    1) 발급:'authorization_code', 2) 갱신:'refresh_token', 3) 삭제: 'delete'
    private String grantType = "authorization_code";
    private String error = "";
    private String errorDescription = "";
    private OAuthToken token = new OAuthToken();

    @Getter
    @Setter
    public static class OAuthToken {
        // 접근 토큰, 발급 후 expires_in 파라미터에 설정된 시간(초)이 지나면 만료됨
        private String accessToken = "";
        // 갱신 토큰, 접근 토큰이 만료될 경우 접근 토큰을 다시 발급받을 때 사용
        private String refreshToken = "";
        // 접근 토큰의 타입으로 Bearer와 MAC의 두 가지를 지원
        private String tokenType = "";
        // 접근 토큰의 유효 기간(초 단위)
        private String expiresIn = "";
        // 구글 아이디 토큰
        private String idToken = "";
    }

}
