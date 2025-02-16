package com.demo.jwt.model;

/**
 * com.demo.jwt.model
 *
 * @author : idasom
 * @data : 2/14/25
 */
public class JwtToken {
    private String accessToken;
    private String refreshToken;

    public JwtToken(String accessToken, String refreshToken) {
        this.accessToken=accessToken;
        this.refreshToken=refreshToken;
    }
    public String getAccessToken() {
        return accessToken;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
}
