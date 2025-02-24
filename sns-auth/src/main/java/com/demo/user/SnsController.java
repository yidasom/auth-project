package com.demo.user;

import com.demo.config.SnsConfig;
import com.demo.config.SnsType;
import jakarta.servlet.http.HttpSession;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * com.demo.controller
 *
 * @author : idasom
 */
@RestController
@RequestMapping("/auth/sns")
public class SnsController {

    private final SnsConfig snsConfig;

    public SnsController(SnsConfig snsConfig) {
        this.snsConfig = snsConfig;
    }


    @GetMapping("/naver/callback")
    public ResponseEntity<?> getNaver(@ModelAttribute SnsDTO snsDTO, HttpSession session, RedirectAttributes attributes) throws UnsupportedEncodingException, URISyntaxException {
        String state = (String) session.getAttribute("state");
        if (!snsDTO.getState().equals(state)) {
            attributes.addFlashAttribute("message", "잘못 된 접근입니다.");
//            return "redirect:/uat/uia/egovLoginUsr.do";
        }

        String callBackUri = snsConfig.getCallback(SnsType.NAVER);
        String redirectURI = URLEncoder.encode(callBackUri, StandardCharsets.UTF_8.toString());

        URI uri = new URIBuilder("https://nid.naver.com/oauth2.0/token")
                .addParameter("grant_type", snsDTO.getGrantType())
                .addParameter("client_id", snsConfig.getId(SnsType.NAVER))
                .addParameter("client_secret", snsConfig.getSecret(SnsType.NAVER))
                .addParameter("redirect_uri", redirectURI)
                .addParameter("code", snsDTO.getCode())
                .addParameter("state", state)
                .build();

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/x-www-form-urlencoded");

        return ResponseEntity.ok().build();
    }

    @GetMapping("/kakao")
    public ResponseEntity<?> getKakao() {
        return ResponseEntity.ok().build();
    }
}
