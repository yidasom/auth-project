package com.demo.config;

import com.demo.config.authentication.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * com.demo.config
 *
 * @author : idasom
 * @data : 5/3/25
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationProvider customAuthenticationProvider;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // csrf 비활성화 (쿠키 사용 X)
                .cors(Customizer.withDefaults()) // cors 비활성화 (front와 연결시 따로 설정)
                .httpBasic(httpbasic -> httpbasic.disable()) // 기본 인증 로그인 비활
//                .formLogin(login -> login.disable()) // 사용하지 않을 때
                .formLogin(login -> login.loginPage("/auth/login")
                        .defaultSuccessUrl("/main")
                        .failureUrl("/user/login?success=500")
                        .usernameParameter("uid")
                        .passwordParameter("pwd"))
//                .logout(logout -> logout.disable()) // 사용하지 않을 때
                .logout(logout -> logout.invalidateHttpSession(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
                        .logoutSuccessUrl("/user/logout?success=200"))
                .headers(header -> header.frameOptions(fro -> fro.disable()).disable()) // x frome option 비활성화
                // ALWAYS: 항상 세션 생성, IF_REQUIRED: 필요시 생성(기본), NEVER: 스프링 시큐리티가 생성하지 않지만, 기존에 존재하면 사용
                // STATLESS: 시큐리티 생성하지않고, 기존 것을 사용하지 않음 (JWT와 같은 토큰 방식 사용)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(customAuthenticationProvider)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/api/auth/login", "/api/auth/logout",
                                "/v3/api-docs/**", "/api-docs/**", "/swagger-resources/**",
                                "/swagger-ui/**", "/webjars/**"
                        ).permitAll() // Swagger, API 문서 허용
                        .anyRequest().authenticated()
                )
        ;

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
