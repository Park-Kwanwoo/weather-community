package org.project.weathercommunity.config.security.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.weathercommunity.config.security.token.JwtTokenProvider;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.domain.token.TokenEditor;
import org.project.weathercommunity.response.error.ErrorResponse;
import org.project.weathercommunity.service.token.TokenService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component

// 토큰 인증 필터
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;

    // 인증 제외할 url
    private static final List<String> EXCLUDE_URL = List.of(
            "/members/join",
            "/members/login",
            "/weather/forecast",
            "/posts",
            "/comments"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtTokenProvider.resolveToken(request);
        if (accessToken != null) {

            // accessToken이 만료 되지 않았다면
            if (jwtTokenProvider.validTokenExpired(accessToken)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // accessToken이 만료 되었다면
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                Member savedMember = (Member) authentication.getPrincipal();
                String refreshToken = tokenService.getRefreshToken(savedMember);

                // refreshToken이 만료 되지 않았다면
                if (jwtTokenProvider.validTokenExpired(refreshToken)) {
                    String reAccessToken = jwtTokenProvider.createAccessToken(savedMember.getEmail());
                    String reRefreshToken = jwtTokenProvider.createRefreshToken(savedMember.getEmail());

                    TokenEditor tokenEditor = TokenEditor.builder()
                            .accessToken(reAccessToken)
                            .refreshToken(reRefreshToken)
                            .build();

                    tokenService.reIssueToken(tokenEditor, savedMember);

                } else {
                    // refreshToken이 만료 되었다면
                    tokenService.deleteToken(savedMember);
                    setErrorResponse(response);
                    SecurityContextHolder.clearContext();
                }
            }
        } else {
            setErrorResponse(response);
        }
        filterChain.doFilter(request, response);
    }

    private void setErrorResponse(HttpServletResponse response) throws IOException {

        response.setStatus(401);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        ErrorResponse body = ErrorResponse.builder()
                .code("401")
                .message("UnAuthorized")
                .validation(Map.of("token", "invalid"))
                .build();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        // 1. 일단 포함되어 있으며,
        // 2. /posts/ && GET
        // 3. /members/join
        // 4. 나머지는 false
        return EXCLUDE_URL.stream().anyMatch(exclude -> {
            if (exclude.equalsIgnoreCase(request.getRequestURI())) {
                return true;
            } else if (request.getRequestURI().startsWith("/posts") && request.getMethod().equals("GET") && exclude.equalsIgnoreCase("/posts")) {
                return true;
            } else {
                return false;
            }
        });
    }
}
