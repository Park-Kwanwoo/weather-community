package org.project.weathercommunity.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * status -> 400
 */
public class LoginValueEmptyException extends WeatherCommunityAuthenticationException {

    private static final String MESSAGE = "잘못된 요청입니다.";

    public LoginValueEmptyException() {
        super(MESSAGE);
    }

    public LoginValueEmptyException(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return BAD_REQUEST.value();
    }
}
