package com.example.interfolio.config;

import com.example.interfolio.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService; // 사용자 정보 처리 서비스
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/auth/**").permitAll() // 해당 경로로 오는 모든 요청 허용
                        .anyRequest().authenticated()
                )
                // OAuth2 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        // 구글에서 사용자 정보를 받아오기
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // 커스텀 서비스에게 처리를 위임
                        )
                        // 로그인 성공 시 이동할 기본 경로 설정
                        .defaultSuccessUrl("http://localhost:5173", true) // 성공 시 리다이렉트
                );

        return http.build();
    }
}
