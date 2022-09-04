package org.project.weathercommunity.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class VueAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String errMsg = "아이디 혹은 비밀번호가 맞지 않습니다.";

        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);

        if (exception instanceof BadCredentialsException) {
            errMsg = "아이디 혹은 비밀번호가 맞지 않습니다.";
        } else if (exception instanceof DisabledException) {
            errMsg = "Locked";
        } else if (exception instanceof CredentialsExpiredException) {
            errMsg = " Expired password";
        }

        objectMapper.writeValue(response.getWriter(), errMsg);
    }
}
