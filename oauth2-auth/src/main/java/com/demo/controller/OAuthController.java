package com.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * com.demo.controller
 *
 * @author : idasom
 */
@RestController
@RequestMapping("/auth/oauth")
public class OAuthController {
    // 사용자 정보 get
    @GetMapping("/google/user")
    public OAuth2User getUser(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return oAuth2User;
    }
//    @GetMapping("/azure/user")

}
