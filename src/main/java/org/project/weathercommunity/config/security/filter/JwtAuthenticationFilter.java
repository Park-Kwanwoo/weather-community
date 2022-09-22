package org.project.weathercommunity.config.security.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.weathercommunity.config.security.token.JwtAuthenticationToken;
import org.project.weathercommunity.domain.member.Member;
import org.project.weathercommunity.domain.token.TokenEditor;
import org.project.weathercommunity.exception.JwtExpiredException;
import org.project.weathercommunity.request.token.TokenRequest;
import org.project.weathercommunity.service.token.TokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
// 토큰 인증을 담당하는 클래스
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtAuthenticationToken jwtAuthenticationToken;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("JwtAuthenticationFilter 진입");

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String accessToken = jwtAuthenticationToken.resolveToken(request);
            log.info("accessToken = {}", accessToken);


            if (accessToken != null && jwtAuthenticationToken.validTokenExpired(accessToken)) {         // accessToken이 Null이 아니면서 만료되지 읺았을 때
                log.info("token이 만료 되지 않았습니다.");
                Authentication authentication = jwtAuthenticationToken.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else if (accessToken != null && !jwtAuthenticationToken.validTokenExpired(accessToken)){  // accessToken이 Null이 아니면서 만료되었을 때

                log.info("token이 만료 되었습니다.");
                Authentication authentication = jwtAuthenticationToken.getAuthentication(accessToken);
                Member member = (Member) authentication.getPrincipal();

                String refreshToken = tokenService.getRefreshToken(member);

                log.info("refreshToken 만료 여부 확인 중..");
                if (jwtAuthenticationToken.validTokenExpired(refreshToken)) {  // accessToken이 Null이 아니면서 만료되고, refreshToken은 살아 있을 때

                    log.info("refreshToken이 만료되지 않았습니다.");
                    log.info("토큰 재발급 중..");
                    String reAccessToken = jwtAuthenticationToken.createAccessToken(member.getEmail());
                    String reRefreshToken = jwtAuthenticationToken.createRefreshToken(member.getEmail());

                    TokenEditor tokenEditor = TokenEditor.builder()
                            .accessToken(reAccessToken)
                            .refreshToken(reRefreshToken)
                            .build();

                    tokenService.reIssueToken(tokenEditor, member);
                    authentication = jwtAuthenticationToken.getAuthentication(reAccessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    log.info("refreshToken 만료. 재 로그인 부탁드립니다.");
                    tokenService.deleteToken(member);

                    throw new JwtExpiredException();
                }

            }
        } else {
            SecurityContextHolder.getContext().getAuthentication();
        }

        log.info("JwtAuthenticationFilter 통과");

        filterChain.doFilter(request, response);
    }

}
