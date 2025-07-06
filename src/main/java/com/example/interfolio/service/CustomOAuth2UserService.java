package com.example.interfolio.service;

import com.example.interfolio.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userReq) {
        OAuth2User principal = super.loadUser(userReq);

        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");

        // DB에 사용자 저장 또는 조회
        User user = userService.getOrCreateUser(email, name);

        // Security Context에 저장할 유저 반환
        return new DefaultOAuth2User(
                Collections.singleton(() -> "ROLE_USER"), // 현재 사용자의 권한 설정
                principal.getAttributes(), // 사용자 정보 전체
                "email" // 식별키
        );
    }
}
