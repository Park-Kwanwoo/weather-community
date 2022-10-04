package org.project.weathercommunity.config.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
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
// 토큰 인증을 담당하는 클래스
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;

    // 인증 제외할 url
    private static final List<String> EXCLUDE_URL = List.of(
            "/weather/**",
            "/post/totalPage"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("JwtAuthenticationFilter 진입");

        String accessToken = jwtTokenProvider.resolveToken(request);
        if (accessToken != null) {

            // 발급된 토큰이 만료 되었다면
            if (jwtTokenProvider.validTokenExpired(accessToken)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                Member savedMember = (Member) authentication.getPrincipal();
                String refreshToken = tokenService.getRefreshToken(savedMember);

                if (jwtTokenProvider.validTokenExpired(refreshToken)) {
                    String reAccessToken = jwtTokenProvider.createAccessToken(savedMember.getEmail());
                    String reRefreshToken = jwtTokenProvider.createRefreshToken(savedMember.getEmail());

                    TokenEditor tokenEditor = TokenEditor.builder()
                            .accessToken(reAccessToken)
                            .refreshToken(reRefreshToken)
                            .build();

                    tokenService.reIssueToken(tokenEditor, savedMember);

                } else {
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

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return EXCLUDE_URL.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));
    }
}
