package com.example.common.security;


import com.example.common.role.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.DelegationTokenExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String token;
            // 헤더가 null 이 아니고 올바른 토큰이라면
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                // 토큰 추출
                token = authorizationHeader.substring(7);
                // 만료 체크
                if (jwtProvider.isExpiration(token)) {
                    log.info("access token 만료");
                    throw new DelegationTokenExpiredException("token 만료");
                }

                String loginId = jwtProvider.getLoginId(token);
                Role role = jwtProvider.getRoles(token);

                PrincipalDetails principalDetails = new PrincipalDetails(new MemberDto(loginId, role));

                // 인증 정보 생성
                Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null,
                        principalDetails.getAuthorities());

                // SecurityContextHolder에 인증 정보 설정
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // response 세팅
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8"); // JSON 응답을 UTF-8로 설정
            response.setContentType(APPLICATION_JSON_VALUE);

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter()
                    .write(e.getMessage());
        }
    }
}
