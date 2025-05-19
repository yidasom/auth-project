package com.demo.util;

import com.demo.auth.provider.JwtTokenProvider;
import com.demo.member.dto.MemberDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtTokenUtil {

    private static final int accessKeyTime = 60 * 60;  // 60분

    private static final int refreshKeyTime = 60 * 60 * 24 * 2; // 2일

    private static JwtTokenProvider jwtTokenProvider;

    public JwtTokenUtil(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public static Date makeExpDate(long accessTokenInTime) throws Exception {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        ZonedDateTime exp = now.plusSeconds(accessTokenInTime);
        return Date.from(exp.toInstant());
    }

    public static ResponseCookie setCookie(String name, String accessToken) {
        ResponseCookie cookie = ResponseCookie.from(name, accessToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Strict")
                .maxAge(accessKeyTime)
                .build();
        return cookie;
    }

    public static void createCookie(MemberDTO memberDTO, Authentication authentication, HttpServletResponse response) throws Exception {
        Date accessTime = makeExpDate(accessKeyTime);
        Date refreshTime = makeExpDate(refreshKeyTime);

        memberDTO.setAccessExp(accessTime.getTime());
        memberDTO.setRefreshExp(refreshTime.getTime());

        ResponseCookie accessCookie = setCookie("accessToken", jwtTokenProvider.generateToken(authentication, memberDTO, accessTime, "accessToken"));
        ResponseCookie refreshCookie = setCookie("refreshToken", jwtTokenProvider.generateToken(authentication, memberDTO, refreshTime, "refreshToken"));

        response.addHeader("Set-Cookie", refreshCookie.toString());
        response.addHeader("Set-Cookie", accessCookie.toString());
    }

}
