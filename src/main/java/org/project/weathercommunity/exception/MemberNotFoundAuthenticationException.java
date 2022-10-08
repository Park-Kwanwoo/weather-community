package org.project.weathercommunity.exception;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * status -> 401
 */
public class MemberNotFoundAuthenticationException extends WeatherCommunityAuthenticationException {

    private static final String MESSAGE = "등록되지 않은 사용자 입니다.";

    public MemberNotFoundAuthenticationException() {
        super(MESSAGE);
    }
    public MemberNotFoundAuthenticationException(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return UNAUTHORIZED.value();
    }
}
