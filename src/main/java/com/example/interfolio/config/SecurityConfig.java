package com.example.interfolio.config;

import com.example.interfolio.entity.User;
import com.example.interfolio.repository.UserRepository;
import com.example.interfolio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import java.time.LocalDateTime;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/auth/**").permitAll() // 해당 경로에 대한 모든 요청 허용
                        .anyRequest().authenticated()
                )
                // OAuth2 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        // 로그인 성공 시 동작할 핸들러 등록
                        .successHandler((request, response, authentication) -> {
                            // 사용자 정보 가져오기
                            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                            String email = oAuth2User.getAttribute("email");
                            String name = oAuth2User.getAttribute("name");

                            // DB에서 해당 이메일의 사용자 조회 시도
                            User user;
                            try {
                                user = userService.getUserByEmail(email);
                            } catch (RuntimeException e) {
                                // DB에 해당 사용자가 없으면 새로 생성
                                User newUser = new User(null, email, name, "google", LocalDateTime.now());
                                user = userService.createUser(newUser);
                            }

                            // JWT 토큰 생성 및 프론트엔드로 전달 (선택 사항)
                            response.sendRedirect("http://localhost:5173?token=your_jwt_token");
                        })
                );

        return http.build();
    }
}