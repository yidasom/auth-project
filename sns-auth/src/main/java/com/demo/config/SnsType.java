package com.demo.config;

/**
 * com.demo.config
 *
 * @author : idasom
 */
public enum SnsType {
    NAVER("naver"), KAKAO("kakao"), GOOGLE("google");

    private final String snsType;

    SnsType(String snsType) {
        this.snsType = snsType;
    }

    public String getSnsType() {
        return snsType;
    }
}
