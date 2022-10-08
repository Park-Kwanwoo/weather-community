package org.project.weathercommunity.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.project.weathercommunity.exception.LoginValueEmptyException;
import org.project.weathercommunity.exception.MemberNotFoundAuthenticationException;
import org.project.weathercommunity.exception.PasswordNotMatchAuthenticationException;
import org.project.weathercommunity.exception.WeatherCommunityAuthenticationException;
import org.project.weathercommunity.response.error.ErrorResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class VueAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        WeatherCommunityAuthenticationException exception = (WeatherCommunityAuthenticationException) authException;
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setStatus(UNAUTHORIZED.value());

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(exception.getStatusCode()))
                .message(exception.getMessage())
                .validation(exception.getValidation())
                .build();

        objectMapper.writeValue(response.getWriter(), body);

    }
}
