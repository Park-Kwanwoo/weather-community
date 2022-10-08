package org.project.weathercommunity.exception;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * status -> 401
 */
public class PasswordNotMatchAuthenticationException extends WeatherCommunityAuthenticationException{

    private static final String MESSAGE = "유효하지 않은 사용자 정보 입니다.";

    public PasswordNotMatchAuthenticationException() {
        super(MESSAGE);
    }

    public PasswordNotMatchAuthenticationException(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return UNAUTHORIZED.value();
    }
}
