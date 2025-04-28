package com.demo.jwt.service.impl;

import com.demo.jwt.dto.LoginDTO;
import com.demo.jwt.entity.UserEntity;
import com.demo.jwt.repository.UserEntityRepository;
import com.demo.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * com.demo.jwt.service.impl
 *
 * @author : idasom
 * @data : 4/28/25
 */
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final UserEntityRepository userEntityRepository;
//    private final ModelMapper

    // 저장 하면서 최종 로그인 일시 추가 update
    @Transactional(value = "transactionManager")
    public void updateAfterUserLoginSuccess(long userSeq, String refreshToken, HttpServletRequest request) throws Exception {
        UserEntity user = userEntityRepository.findById(userSeq).get();
        userEntityRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginDTO loginDTO = null;
        UserEntity principal = (UserEntity) userEntityRepository.findByUserId(username)
                .orElseThrow(() -> {
                    return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. : " + username);
                });

        return loginDTO;
    }
}
