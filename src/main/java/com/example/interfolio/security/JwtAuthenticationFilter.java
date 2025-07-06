package com.example.interfolio.security;

import com.example.interfolio.service.UserService;
import com.example.interfolio.security.JwtUtil;
import com.example.interfolio.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, jakarta.servlet.ServletException {

        // 요청 헤더에서 "Authorization" 값을 가져옴
        String header = req.getHeader("Authorization");

        // 헤더가 "Bearer {토큰}" 형식이면 처리 시작
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7); // "Bearer " 이후 토큰만 추출

            // 토큰이 유효한 경우만 처리
            if (jwtUtil.validateToken(token)) {
                String email = jwtUtil.getEmailFromToken(token); // 토큰에서 이메일 추출
                User user = userService.getUserByEmail(email);   // 이메일로 DB에서 사용자 조회

                // 인증 객체를 만들어 SecurityContext에 등록
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                user,                      // 사용자 정보
                                null,                      // 비밀번호 (필요 없음)
                                List.of(new SimpleGrantedAuthority("ROLE_USER")) // 권한 부여
                        )
                );
            }
        }

        // 다음 필터 또는 컨트롤러로 요청 전달
        chain.doFilter(req, res);
    }
}
