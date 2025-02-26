package com.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * com.demo.config
 *
 * @author : idasom
 */
@Getter
@Setter
@Component
public class SnsConfig {
    @Value("sns.login.naver.callback")
    private String naverCallback;
    @Value("sns.login.naver.id")
    private String naverId;
    @Value("sns.login.naver.secret")
    private String naverSecret;

    @Value("sns.login.kakao.callback")
    private String kakaoCallback;
    @Value("sns.login.kakao.id")
    private String kakaoId;

    @Value("sns.login.google.callback")
    private String googleCallback;
    @Value("sns.login.google.id")
    private String googleId;
    @Value("sns.login.google.secret")
    private String googleSecret;

    public String getId(SnsType type) {
        return switch (type) {
            case NAVER -> naverId;
            case KAKAO -> kakaoId;
            case GOOGLE -> googleId;
        };
    }

    public String getCallback(SnsType type) {
        return switch (type) {
            case NAVER -> naverCallback;
            case KAKAO -> kakaoCallback;
            case GOOGLE -> googleCallback;
        };
    }

    public String getSecret(SnsType type) {
        return switch (type) {
            case NAVER -> naverSecret;
            case GOOGLE -> googleSecret;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}
