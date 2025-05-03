package com.sample.jwt;

import com.sample.jwt.dto.LoginDTO;
import com.sample.jwt.dto.UserDTO;
import com.sample.jwt.service.JwtService;
import com.sample.jwt.util.JwtBlacklistService;
import com.sample.jwt.util.JwtProvider;
import com.sample.util.SecurityUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * com.demo.jwt.util
 *
 * @author : idasom
 */
@RestController
@RequestMapping("/auth/jwt")
@SecurityRequirement(name = "bearerAuth")
public class JwtController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final int accessKeyTime = 60 * 60;  // 60분  // 쿠기가 살아있는 시간
    private final int refreshKeyTime = 60 * 60 * 24 * 2; // 2일

    private JwtProvider jwtProvider;
    private JwtBlacklistService jwtBlacklistService;
    private JwtService jwtService;

    @Autowired
    public JwtController(AuthenticationManagerBuilder authenticationManagerBuilder, JwtProvider jwtProvider, JwtBlacklistService jwtBlacklistService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtProvider = jwtProvider;
        this.jwtBlacklistService = jwtBlacklistService;
    }

    /**
     * 로그인 후 JWT 토큰 발급
     */
    @PostMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginDTO loginDTO) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        UserDTO userDTO = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginDTO.getUsername()
                    , SecurityUtil.encryptPassword(String.valueOf(loginDTO.getPassword()))
            );
            // 인증 확인
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            // 인증 확인된 정보를 securityContextHolder에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            userDTO = (UserDTO) authentication.getDetails();

            jwtService.updateAfterUserLoginSuccess(userDTO.getUserSeq(), "", request);

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
     * 토큰 유효성 검증
     */
    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        boolean isValid = jwtProvider.validateToken(token);
        return new ResponseEntity<>(isValid, HttpStatus.OK);
    }

    /**
     * 토큰에서 사용자 정보 추출
     */
    @GetMapping("/user")
    public ResponseEntity<?> getUserFromToken(@RequestParam String token) {
        String user = jwtProvider.getUsername(token);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * 로그아웃 (토큰을 블랙리스트에 추가)
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String token) {
        jwtBlacklistService.addToBlacklist(token);
        return new ResponseEntity<>("로그아웃되었습니다. 클라이언트에서 토큰을 삭제하세요.", HttpStatus.OK);
    }

}
