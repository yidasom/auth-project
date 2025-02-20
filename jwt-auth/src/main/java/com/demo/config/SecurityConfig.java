package com.demo.config;

import com.demo.jwt.util.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * com.demo.config
 *
 * @author : idasom
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // csrf 비활성화 (쿠키 사용 X)
                .cors(cors -> cors.disable()) // cors 비활성화 (front와 연결시 따로 설정)
                .httpBasic(httpbasic -> httpbasic.disable()) // 기본 인증 로그인 비활
                .logout(logout -> logout.disable())
                .headers(header -> header.frameOptions(fro -> fro.disable()).disable()) // x frome option 비활성화
                // ALWAYS: 항상 세션 생성, IF_REQUIRED: 필요시 생성(기본), NEVER: 스프링 시큐리티가 생성하지 않지만, 기존에 존재하면 사용
                // STATLESS: 시큐리티 생성하지않고, 기존 것을 사용하지 않음 (JWT와 같은 토큰 방식 사용)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/auth/jwt/login", "/auth/jwt/logout")
                        .permitAll() // react page 처리용
                        .anyRequest().authenticated()
                )
//                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(new JwtTokenExceptionFilter(), jwtTokenFilter.getClass())

//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .loginProcessingUrl("/loginProc")
//                        .permitAll()
//                )
        ;

        return http.build();
    }
}
