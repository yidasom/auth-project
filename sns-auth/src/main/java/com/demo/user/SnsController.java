package com.demo.user;

import com.demo.config.SnsConfig;
import com.demo.config.SnsType;
import com.demo.user.SnsOAuthApi.OAuthToken;
import com.demo.util.UrlConnectionUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Collections;
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
    public ResponseEntity<?> getNaver(@ModelAttribute SnsOAuthApi snsOAuthApi, HttpSession session, RedirectAttributes attributes) throws UnsupportedEncodingException, URISyntaxException {
        String state = (String) session.getAttribute("state");
        if (!snsOAuthApi.getState().equals(state)) {
            attributes.addFlashAttribute("message", "잘못 된 접근입니다.");
        }

        String callBackUri = snsConfig.getCallback(SnsType.NAVER);
        String redirectURI = URLEncoder.encode(callBackUri, StandardCharsets.UTF_8.toString());

        String apiUri = "https://nid.naver.com/oauth2.0/token";
        StringBuilder sb = new StringBuilder();
        sb.append("grant_type=").append(URLEncoder.encode(snsOAuthApi.getGrantType(), StandardCharsets.UTF_8))
                .append("&client_id=").append(URLEncoder.encode(snsConfig.getId(SnsType.NAVER), StandardCharsets.UTF_8))
                .append("&client_secret=").append(URLEncoder.encode(snsConfig.getSecret(SnsType.NAVER), StandardCharsets.UTF_8))
                .append("&redirect_uri=").append(URLEncoder.encode(redirectURI, StandardCharsets.UTF_8))
                .append("&code=").append(URLEncoder.encode(snsOAuthApi.getCode(), StandardCharsets.UTF_8))
                .append("&state=").append(URLEncoder.encode(state, StandardCharsets.UTF_8));


        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/x-www-form-urlencoded");
        String responseBody = UrlConnectionUtil.post(apiUri, requestHeaders, sb.toString());


        // 회원 확인
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        snsOAuthApi = gson.fromJson(responseBody, SnsOAuthApi.class);
        if (!"".equals(snsOAuthApi.getError())) {
            attributes.addFlashAttribute("message", snsOAuthApi.getErrorDescription());
        }

        // 회원 프로필 조회
        apiUri = "https://openapi.naver.com/v1/nid/me";
        String header = "Bearer " + new OAuthToken().getAccessToken();
        requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);
        responseBody = UrlConnectionUtil.post(apiUri, requestHeaders, null);

        SnsOAuthApiProfile mberApi = gson.fromJson(responseBody, SnsOAuthApiProfile.class);


        // 회원 프로필 조회 가능 여부
        if ("00".equals(mberApi.getResultcode())) {
            // 회원 조회 로직 : Boolean chkLogin = memberService.findById(value.id);
            Boolean chkLogin = false;
            if (Boolean.TRUE.equals(chkLogin)) {
                attributes.addFlashAttribute("message", "네이버 아이디를 연동하였습니다.");
            } else {
                if (chkLogin != null) {
                    attributes.addFlashAttribute("message", "연동에 실패하였습니다. 이미 연동된 아이디인지 확인부탁드립니다.");
                } else {
                    // sns user profile 저장 로직
//                    LoginVO loginVO = memberService.save(value);
//                    if (loginVO != null) {
//                        // 캐시 저장 등 이후 로직 추가
//                    } else {
//                        attributes.addFlashAttribute("message", "sns 로그인에 실패하였습니다.");
//                    }
                }
            }
        } else {
            attributes.addFlashAttribute("message", mberApi.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<?> getKakao(@ModelAttribute SnsOAuthApi snsOAuthApi, HttpSession session, RedirectAttributes attributes) throws UnsupportedEncodingException {
        // state 확인
        String state = (String) session.getAttribute("state");
        if (!snsOAuthApi.getState().equals(state)) {
            attributes.addFlashAttribute("message", "잘못 된 접근입니다.");
        }

        String callBackUri = snsConfig.getCallback(SnsType.KAKAO);
        String redirectURI = URLEncoder.encode(callBackUri, StandardCharsets.UTF_8.toString());

        String apiUri = "https://kauth.kakao.com/oauth/token";
        StringBuilder sb = new StringBuilder();
        sb.append("grant_type=").append(URLEncoder.encode(snsOAuthApi.getGrantType(), StandardCharsets.UTF_8))
                .append("&client_id=").append(URLEncoder.encode(snsConfig.getId(SnsType.KAKAO), StandardCharsets.UTF_8))
                .append("&redirect_uri=").append(URLEncoder.encode(redirectURI, StandardCharsets.UTF_8))
                .append("&code=").append(URLEncoder.encode(snsOAuthApi.getCode(), StandardCharsets.UTF_8));


        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/x-www-form-urlencoded");
        String responseBody = UrlConnectionUtil.post(apiUri, requestHeaders, apiUri);

        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        snsOAuthApi = gson.fromJson(responseBody, SnsOAuthApi.class);
        if (!"".equals(snsOAuthApi.getError())) {
            attributes.addFlashAttribute("message", snsOAuthApi.getErrorDescription());
        }

        // 회원 프로필 조회
        apiUri = "https://kapi.kakao.com/v2/user/me";
        String header = "Bearer " + new OAuthToken().getAccessToken();
        requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);
        responseBody = UrlConnectionUtil.post(apiUri, requestHeaders, null);

        SnsOAuthApiProfile mberApi = gson.fromJson(responseBody, SnsOAuthApiProfile.class);

        // 회원 프로필 조회 가능 여부 userId 체크
        if (StringUtils.isEmpty(mberApi.getCode())) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) mberApi.getKakaoAccount();
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

//            Boolean chkLogin = chkSnsLogin(mbeEmplyrVO); // 회원 조회 로직
            Boolean chkLogin = false;
            if (Boolean.TRUE.equals(chkLogin)) {
                attributes.addFlashAttribute("message", "카카오 아이디를 연동하였습니다.");
            } else {
                if (chkLogin != null) {
                    attributes.addFlashAttribute("message", "연동에 실패하였습니다. 이미 연동된 아이디인지 확인부탁드립니다.");
                } else {
                    // sns user profile 저장 로직
//                    LoginVO loginVO = memberService.save(value);
//                    if (loginVO != null) {
//                        // 캐시 저장 등 이후 로직 추가
//                    } else {
//                        attributes.addFlashAttribute("message", "sns 로그인에 실패하였습니다.");
//                    }

                }
            }
        } else {
            attributes.addFlashAttribute("message", mberApi.getMsg());
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/google/callback", produces = "text/plain; charset=utf-8")
    public ResponseEntity<?> googleCallback(@ModelAttribute SnsOAuthApi snsOAuthApi, ModelMap model, HttpSession session,
                                            RedirectAttributes attributes) throws GeneralSecurityException, IOException {
        // state 비교
        if (!snsOAuthApi.getState().equals(session.getAttribute("state"))) {
            attributes.addFlashAttribute("message", "잘못 된 접근입니다.");
        }

        // 토큰 발급 API
        String apiURL = "https://oauth2.googleapis.com/token";
        StringBuilder sb = new StringBuilder();
        sb.append("grant_type=").append(URLEncoder.encode(snsOAuthApi.getGrantType(), StandardCharsets.UTF_8))
                .append("&client_id=").append(URLEncoder.encode(snsConfig.getId(SnsType.GOOGLE), StandardCharsets.UTF_8))
                .append("&client_secret=").append(URLEncoder.encode(snsConfig.getSecret(SnsType.GOOGLE), StandardCharsets.UTF_8))
                .append("&redirect_uri=").append(URLEncoder.encode(snsConfig.getCallback(SnsType.GOOGLE), StandardCharsets.UTF_8))
                .append("&code=").append(URLEncoder.encode(snsOAuthApi.getCode(), StandardCharsets.UTF_8));

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/x-www-form-urlencoded");
        String responseBody = UrlConnectionUtil.post(apiURL, requestHeaders, sb.toString());

        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        snsOAuthApi = gson.fromJson(responseBody, SnsOAuthApi.class);

        // 토큰 발급 에러체크
        if (!"".equals(snsOAuthApi.getError())) {
            attributes.addFlashAttribute("message", snsOAuthApi.getErrorDescription());
        }

        // 회원 정보 API
        HttpTransport httpTransport = Utils.getDefaultTransport();
        JsonFactory jsonFactory = Utils.getDefaultJsonFactory();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
                .setAudience(Collections.singletonList(snsConfig.getId(SnsType.GOOGLE))).build();

        GoogleIdToken idToken = verifier.verify(new OAuthToken().getIdToken());
        if (idToken != null) {
            Payload payload = idToken.getPayload();

            String userId = payload.getSubject();
//			String email = payload.getEmail();
//			String name = (String) payload.get("name");

//            MbeEmplyrVO mbeEmplyrVO = new MbeEmplyrVO();
//            mbeEmplyrVO.setGoogleEsntlId(userId);
//            mbeEmplyrVO.setSnsKnd("google");

//          회원 유무 조회 : Boolean chkLogin = chkSnsLogin(mbeEmplyrVO);
            Boolean chkLogin = false;
            if (Boolean.TRUE.equals(chkLogin)) {
                attributes.addFlashAttribute("message", "구글 아이디를 연동하였습니다.");
            } else {
                if (chkLogin != null) {
                    attributes.addFlashAttribute("message", "연동에 실패하였습니다. 이미 연동된 아이디인지 확인부탁드립니다.");
                } else {
                    // sns user profile 저장 로직
//                    LoginVO loginVO = memberService.save(value);
//                    if (loginVO != null) {
//                        // 캐시 저장 등 이후 로직 추가
//                    } else {
//                        attributes.addFlashAttribute("message", "sns 로그인에 실패하였습니다.");
//                    }
                }
            }
        } else {
            attributes.addFlashAttribute("message", "Invalid ID token.");
        }
        return ResponseEntity.ok().build();
        // oauth2 방식으로 사용자 정보 조회(권장하지 않음 by구글)
//		String apiURL;
//		apiURL = "https://oauth2.googleapis.com/tokeninfo?id_token=" + request.getParameter("token");
//		Map<String, String> requestHeaders = new HashMap<>();
//		String responseBody = UrlConnectionUtil.get(apiURL, requestHeaders);
//		System.out.println(responseBody);
    }
}
