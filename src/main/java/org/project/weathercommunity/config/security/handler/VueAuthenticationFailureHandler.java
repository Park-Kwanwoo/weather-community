package org.project.weathercommunity.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.project.weathercommunity.exception.MemberNotFoundAuthenticationException;
import org.project.weathercommunity.response.error.ErrorResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class VueAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");

        if (exception instanceof BadCredentialsException) {
            ErrorResponse body = ErrorResponse.builder()
                    .code(String.valueOf(UNAUTHORIZED))
                    .message("유효하지 않은 사용자 정보입니다.")
                    .validation(Map.of("password", "잘못된 비밀번호 입니다."))
                    .build();
            objectMapper.writeValue(response.getWriter(), body);
        } else if (exception instanceof MemberNotFoundAuthenticationException) {
            ErrorResponse body = ErrorResponse.builder()
                    .code(String.valueOf(UNAUTHORIZED))
                    .message("유효하지 않은 사용자 정보입니다.")
                    .validation(Map.of("email", "등록되지 않은 이메일입니다."))
                    .build();
            objectMapper.writeValue(response.getWriter(), body);
        }
    }
}
