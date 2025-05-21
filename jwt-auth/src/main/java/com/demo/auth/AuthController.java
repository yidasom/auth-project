package com.demo.auth;

import com.demo.auth.dto.LoginDTO;
import com.demo.member.dto.MemberDTO;
import com.demo.member.service.MemberService;
import com.demo.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.util.Base64;

/**
 * com.demo.auth
 *
 * @author : idasom
 * @data : 5/3/25
 */
@RestController
@RequestMapping(value = "/api/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private MemberService memberService;



    /**
     * 비밀번호를 암호화하는 기능(복호화가 되면 안되므로 SHA-256 인코딩 방식 적용)
     *
     * @param data 암호화할 비밀번호
     * @return 암호화된 비밀번호
     * @throws Exception
     */
    public String encryptPassword(String data) throws Exception {
        if (data == null) {
            return "";
        }
        byte[] plainText = null;
        byte[] hashValue = null;

        MessageDigest md;
        plainText = data.getBytes("UTF-8");
        md = MessageDigest.getInstance("SHA-256");
        hashValue = md.digest(plainText);
        return new String(Base64.getEncoder().encode(hashValue));
    }

    /**
     * 로그인 후 JWT 토큰 발급
     *
     * @param request
     * @param response
     * @param loginDTO
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    public String authenticate(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginDTO loginDTO) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        MemberDTO memberDTO = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginDTO.getUsername()
                    , encryptPassword(String.valueOf(loginDTO.getPassword()))
            );
            // 인증 확인
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            // 인증 확인된 정보를 securityContextHolder에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            memberDTO = (MemberDTO) authentication.getDetails();

            if ("cookie".equals(loginDTO.getStoreType())) {
                // create cookie
                JwtTokenUtil.createCookie(memberDTO, authentication, response);
            } else if ("redis".equals(loginDTO.getStoreType())) {
                // create redis
                JwtTokenUtil.createRedis(memberDTO, authentication);
            }

            // cookie 에 저장 jwt token만드는 로직 필요
//            ResponseCookie accessCookie = ResponseCookie.from("accessToken", )
//                            .path("/")
//                            .httpOnly(true)
//                            .secure(false)
//                            .sameSite("Strict")
//                            .maxAge(accessKeyTime)
//                            .build();


            memberService.updateAfterUserLoginSuccess(memberDTO.getUuid(), "", request);

//            String token = jwtService.login(userDTO);

            return "redirect:/home";

//        } catch (UsernameNotFoundException | BadCredentialsException | DisabledException | AccountExpiredException e) {
//            TokenModel tokenModel = new TokenModel();
//            tokenModel.setErrorMessage(e.getMessage());
//            return new ResponseEntity<>(userDetail, httpHeaders, 460);
        } catch (Exception e) {
//            log.error("", e);
            return null;
        }
    }

    /**
     * 로그아웃 (토큰을 블랙리스트에 추가)
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String token) {
//        jwtBlacklistService.addToBlacklist(token);
        return new ResponseEntity<>("로그아웃되었습니다. 클라이언트에서 토큰을 삭제하세요.", HttpStatus.OK);
    }

}
