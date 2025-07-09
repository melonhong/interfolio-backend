package com.example.interfolio.config;

import com.example.interfolio.security.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import com.example.interfolio.security.JwtUtil;

@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtUtil jwtUtil;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, JwtUtil jwtUtil) {
        this.customOAuth2UserService = customOAuth2UserService; // 사용자 정보 처리 서비스
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults()) // SpringSecurity의 CORS 설정 활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
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
                        .successHandler((request, response, authentication) -> {
                            // OAuth2 로그인 성공 후 사용자 정보 추출
                            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                            String email = oAuth2User.getAttribute("email");

                            // JWT 생성
                            String token = jwtUtil.generateToken(email);

                            // JWT를 쿼리스트링으로 프론트엔드에 전달
                            response.sendRedirect("http://localhost:5173?token=" + token);
                        })
                )
                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutSuccessUrl("/") // 성공 시 이동할 경로
                        .invalidateHttpSession(true) // 세션 제거
                        .deleteCookies("JSESSIONID") // 쿠키 제거
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        })
                );

        return http.build();
    }
}
