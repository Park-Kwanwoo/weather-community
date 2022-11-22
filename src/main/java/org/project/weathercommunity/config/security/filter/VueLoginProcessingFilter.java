package org.project.weathercommunity.config.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.project.weathercommunity.config.security.token.VueAuthenticationToken;
import org.project.weathercommunity.exception.LoginValueEmptyException;
import org.project.weathercommunity.request.member.MemberLogin;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class VueLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    public VueLoginProcessingFilter(AuthenticationManager authenticationManager, AuthenticationSuccessHandler authenticationSuccessHandler, AuthenticationFailureHandler authenticationFailureHandler) {
        super(new AntPathRequestMatcher("/members/login"));
        setAuthenticationManager(authenticationManager);
        setAuthenticationSuccessHandler(authenticationSuccessHandler);
        setAuthenticationFailureHandler(authenticationFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {

        if (!isAjax(request)) {
            throw new IllegalArgumentException("로그인 오류");
        }

        MemberLogin memberLogin = objectMapper.readValue(request.getReader(), MemberLogin.class);
        VueAuthenticationToken token;

        // 카카오 로그인의 경우
        if (memberLogin.getAuth().equals("kakao")) {

            if (!StringUtils.hasText(memberLogin.getEmail())) {
                throw new LoginValueEmptyException("email", "등록되지 않은 이메일입니다.");
            }

            token = new VueAuthenticationToken(memberLogin.getEmail(), null);
        } else {
            if (!StringUtils.hasText(memberLogin.getPassword()) || !StringUtils.hasText(memberLogin.getEmail())) {
                throw new LoginValueEmptyException("value","이메일과 비밀번호를 제대로 입력해주세요.");
            }
            token = new VueAuthenticationToken(memberLogin.getEmail(), memberLogin.getPassword());

        }
        return getAuthenticationManager().authenticate(token);
    }

    private boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
}
