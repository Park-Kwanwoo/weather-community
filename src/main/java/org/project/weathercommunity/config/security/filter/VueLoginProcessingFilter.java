package org.project.weathercommunity.config.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.project.weathercommunity.config.security.token.VueAuthenticationToken;
import org.project.weathercommunity.request.member.MemberLogin;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class VueLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    public VueLoginProcessingFilter() {
        super(new AntPathRequestMatcher("/members/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        if (!isAjax(request)) {
            throw new IllegalArgumentException("Ajax 로그인 오류");
        }

        MemberLogin memberLogin = objectMapper.readValue(request.getReader(), MemberLogin.class);
        if (!StringUtils.hasText(memberLogin.getPassword()) || !StringUtils.hasText(memberLogin.getEmail())) {
            throw new IllegalArgumentException("Username or Password is Empty");
        }

        VueAuthenticationToken token = new VueAuthenticationToken(memberLogin.getEmail(), memberLogin.getPassword());

        return getAuthenticationManager().authenticate(token);
    }

    private boolean isAjax(HttpServletRequest request) {
        return "true".equals(request.getHeader("valid"));
    }
}
