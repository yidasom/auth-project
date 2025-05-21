package com.demo.auth.provider;

import com.demo.auth.dto.LoginDTO;
import com.demo.member.dto.MemberDTO;
import com.demo.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashSet;
import java.util.Set;

/**
 * com.demo.config.authentication
 *
 * @author : idasom
 * @data : 5/11/25
 */
@Component("CustomAuthenticationProvider")
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private ModelMapper modelMapper;
    private MemberService memberService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();

        String id = "";
        String pw = "";
        try {
            id = (String) authToken.getPrincipal();
            pw = String.valueOf(authToken.getCredentials());

        } catch (Exception e) {
            throw new BadCredentialsException("로그인 처리 중 오류 발생");
        }

        LoginDTO userInfo = null;
        userInfo = (LoginDTO) memberService.loadUserByUsername(id);

        // 오류 처리 시작
        // 존재하지 않는 사용자
        if (userInfo == null) {
            throw new UsernameNotFoundException("아이디 또는 패스워드 오류");
        }

//        if (!"Y".equals(userInfo.getCanLoginYn())) {
//            throw new AccountExpiredException("계정 비활성화 상태");
//        }

        // 로그인 실패 횟수 초과
        if (userInfo.getLoginFailCnt() >= 5) {
            throw new DisabledException("비밀번호 오류 횟수 초과");
        }

        if (!matchPassword(userInfo.getPassword(), pw)) {
            // 비밀번호 오류 횟수 증가
            try {
                // TODO 비밀번호 오류 시 db 저장
            } catch (Exception e) {
                // ignore
            }
            throw new BadCredentialsException("아이디 또는 패스워드 오류");
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        //TODO user role 설정
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_SYSTEM_ADMIN"));
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(id, pw, grantedAuthorities);

        MemberDTO member = new MemberDTO();
        modelMapper.map(userInfo, member);
        member.setUserPswd(null);
        token.setDetails(member);
        return token;
    }

    private boolean matchPassword(String password, Object credentials) {
        boolean match = false;
        try {
//            match = password.equals(SecurityUtil.encryptPassword(String.valueOf(credentials)));
            match = password.equals(credentials);
        } catch (Exception e) {
            log.error("", e);
            match = false;
        }
        return match;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
