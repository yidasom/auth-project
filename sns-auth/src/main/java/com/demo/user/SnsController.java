package com.demo.user;

import com.demo.config.SnsConfig;
import com.demo.config.SnsType;
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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * com.demo.controller
 *
 * @author : idasom
 */
@Controller
//@RequestMapping("/auth/sns")
public class SnsController {

    private final Environment env;
    private final SnsConfig snsConfig;

    @Autowired
    public SnsController(Environment env, SnsConfig snsConfig) {
        this.env = env;
        this.snsConfig = snsConfig;
    }

    @GetMapping("/home")
    public String home(Model model, HttpServletResponse response) {
        String state = new SnsOAuthApi().getState();
        String nonce = new SnsOAuthApi().getNonce();
        Cookie cookie = new Cookie("state", state);
        cookie.setMaxAge(1800); // 30분
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        response.addCookie(cookie);
        cookie = new Cookie("nonce", nonce);
        cookie.setMaxAge(1800); // 30분
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        response.addCookie(cookie);

        model.addAttribute("naver", Map.of(
                "id", env.getProperty("sns.login.naver.id"),
                "secret", env.getProperty("sns.login.naver.secret"),
                "callback", env.getProperty("sns.login.naver.callback"),
                "state", state
        ));
        model.addAttribute("kakao", Map.of(
                "id", env.getProperty("sns.login.kakao.id"),
                "callback", env.getProperty("sns.login.kakao.callback"),
                "state", state
        ));
        model.addAttribute("google", Map.of(
                "id", env.getProperty("sns.login.google.id"),
                "secret", env.getProperty("sns.login.google.secret"),
                "callback", env.getProperty("sns.login.google.callback"),
                "state", state,
                "nonce", nonce
        ));
        model.addAttribute("message", "Hello, Spring Boot!");

        return "home";
    }

    @RequestMapping("/naver/callback")
    public String getNaver(@ModelAttribute SnsOAuthApi snsOAuthApi, @CookieValue(value = "state", required = false) String state, Model model, RedirectAttributes attributes, HttpServletResponse response) throws UnsupportedEncodingException {
        if (!snsOAuthApi.getState().equals(state)) {
            attributes.addFlashAttribute("message", "잘못 된 접근입니다.");
        }

        String callBackUri = snsConfig.getCallback(SnsType.NAVER);
        String redirectUri = URLEncoder.encode(callBackUri, "UTF-8");
        String apiUri = "https://nid.naver.com/oauth2.0/token";
        String params = "grant_type=" + snsOAuthApi.getGrantType();
        params += "&client_id=" + snsConfig.getId(SnsType.NAVER);
        params += "&client_secret=" + snsConfig.getSecret(SnsType.NAVER);
        params += "&redirect_uri=" + redirectUri;
        params += "&code=" + snsOAuthApi.getCode();
        params += "&state=" + state;


        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/x-www-form-urlencoded");
        String responseBody = UrlConnectionUtil.post(apiUri, requestHeaders, params);


        // 회원 확인
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        snsOAuthApi = gson.fromJson(responseBody, SnsOAuthApi.class);
        if (!"".equals(snsOAuthApi.getError())) {
            attributes.addFlashAttribute("message", snsOAuthApi.getErrorDescription());
        }

        // 회원 프로필 조회
        apiUri = "https://openapi.naver.com/v1/nid/me";
        String header = "Bearer " + snsOAuthApi.getAccessToken();
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
            Cookie cookie = new Cookie("access_token", snsOAuthApi.getAccessToken());
            cookie.setMaxAge(1800); // 30분
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            cookie = new Cookie("service_provider", (SnsType.NAVER).toString());
            cookie.setMaxAge(1800); // 30분
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            model.addAttribute("type", (SnsType.NAVER).toString());
            model.addAttribute("profile", mberApi.getResponse());
        } else {
            attributes.addFlashAttribute("message", mberApi.getMessage());
        }
        return "callback";
//        return ResponseEntity.ok().build();
    }

    @RequestMapping("/kakao/callback")
    public String getKakao(@ModelAttribute SnsOAuthApi snsOAuthApi, @CookieValue(value = "state", required = false) String state, Model model, RedirectAttributes attributes) throws UnsupportedEncodingException {
        if (!snsOAuthApi.getState().equals(state)) {
            attributes.addFlashAttribute("message", "잘못 된 접근입니다.");
        }

        String redirectUri = URLEncoder.encode(snsOAuthApi.getCode(), "UTF-8");
        String apiUri = "https://kauth.kakao.com/oauth/token";
        String params = "grant_type=" + snsOAuthApi.getGrantType();
        params += "&client_id=" + snsConfig.getId(SnsType.KAKAO);
        params += "&redirect_uri=" + snsConfig.getCallback(SnsType.KAKAO);
        params += "&code=" + redirectUri;

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/x-www-form-urlencoded");
        String responseBody = UrlConnectionUtil.post(apiUri, requestHeaders, params);

        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        snsOAuthApi = gson.fromJson(responseBody, SnsOAuthApi.class);
        if (!"".equals(snsOAuthApi.getError())) {
            attributes.addFlashAttribute("message", snsOAuthApi.getErrorDescription());
        }

        // 회원 프로필 조회
        apiUri = "https://kapi.kakao.com/v2/user/me";
        String header = "Bearer " + snsOAuthApi.getAccessToken();
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
            model.addAttribute("type", (SnsType.KAKAO).toString());
            model.addAttribute("profile", profile);

        } else {
            attributes.addFlashAttribute("message", mberApi.getMsg());
        }
        return "callback";
//        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/google/callback", produces = "text/plain; charset=utf-8")
    public String googleCallback(@ModelAttribute SnsOAuthApi snsOAuthApi, @CookieValue(value = "state", required = false) String state, @CookieValue(value = "nonce", required = false) String nonce,
                                 RedirectAttributes attributes, HttpServletRequest request, HttpServletResponse response, Model model) throws GeneralSecurityException, IOException {

//        SnsConfig.SnsProperties google = snsConfig.getType(SnsType.GOOGLE);
        // state 비교
        if (!snsOAuthApi.getState().equals(state)) {
            attributes.addFlashAttribute("message", "잘못 된 접근입니다.");
        }

        // 토큰 발급 API
        String apiUri = "https://oauth2.googleapis.com/token";
        String params = "grant_type=" + snsOAuthApi.getGrantType();
        params += "&client_id=" + snsConfig.getId(SnsType.GOOGLE);
        params += "&client_secret=" + snsConfig.getSecret(SnsType.GOOGLE);
        params += "&redirect_uri=" + snsConfig.getCallback(SnsType.GOOGLE);
        params += "&code=" + request.getParameter("code").toString();

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/x-www-form-urlencoded");
        String responseBody = UrlConnectionUtil.post(apiUri, requestHeaders, params);

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

        GoogleIdToken idToken = verifier.verify(snsOAuthApi.getIdToken());
        if (idToken != null) {
            Map<String, String> profile = new HashMap<>();
            Payload payload = idToken.getPayload();

//            String userId = payload.getSubject();
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            profile.put("email", email);
            profile.put("name", name);

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
            Cookie cookie = new Cookie("access_token", snsOAuthApi.getAccessToken());
            cookie.setMaxAge(1800); // 30분
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            model.addAttribute("type", (SnsType.GOOGLE).toString());
            model.addAttribute("profile", profile);
        } else {
            attributes.addFlashAttribute("message", "Invalid ID token.");
        }
        return "callback";

        // oauth2 방식으로 사용자 정보 조회(권장하지 않음 by구글)
//		String apiURL;
//		apiURL = "https://oauth2.googleapis.com/tokeninfo?id_token=" + request.getParameter("token");
//		Map<String, String> requestHeaders = new HashMap<>();
//		String responseBody = UrlConnectionUtil.get(apiURL, requestHeaders);
//		System.out.println(responseBody);
    }

    @RequestMapping("/naver/logout")
    public String logoutNaver(@CookieValue(value = "access_token", required = false) String access_token, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // token 삭제
        try {
            String apiUri = "https://nid.naver.com/oauth2.0/token";
            String params = "grant_type=delete";
            params += "&client_id=" + snsConfig.getId(SnsType.NAVER);
            params += "&client_secret=" + snsConfig.getSecret(SnsType.NAVER);
            params += "&access_token=" + access_token;
            params += "&service_provider=" + (SnsType.NAVER).toString();

            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("Content-Type", "application/x-www-form-urlencoded");
            String responseBody = UrlConnectionUtil.post(apiUri, requestHeaders, params);

            // cookie 삭제
            deleteCookie("access_token", response);
            deleteCookie("service_provider", response);
            deleteCookie("state", response);

            request.getSession().invalidate();

            if (responseBody != null && responseBody.contains("error")) {
                return responseBody;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/home";
    }

    @RequestMapping("/kakao/logout")
    public String logoutKakao(@CookieValue(value = "access_token", required = false) String access_token, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // token 삭제
        try {
            String apiUri = "https://kapi.kakao.com/v1/user/logout";

            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            requestHeaders.put("Authorization", access_token);
            String responseBody = UrlConnectionUtil.post(apiUri, requestHeaders, null);

            if (responseBody != null && responseBody.contains("error")) {
                return responseBody;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/home";
    }

    @RequestMapping("/google/logout")
    public String logoutGoogle(@CookieValue(value = "access_token", required = false) String access_token) {
        String accessToken = access_token.replace("Bearer ", "");
        String revokeUrl = "https://accounts.google.com/o/oauth2/revoke?token=" + accessToken;

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(revokeUrl, null, String.class);

        return "redirect:/home";
    }

    private void deleteCookie(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}
