package org.project.weathercommunity.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class MemberEmailDuplicationException extends WeatherCommunityException {

    private static final String MESSAGE = "중복된 이메일 입니다.";

    public MemberEmailDuplicationException() {
        super(MESSAGE);
    }

    public MemberEmailDuplicationException(Throwable cause) {
        super(MESSAGE, cause);
    }

    public MemberEmailDuplicationException(String fieldName, String message) {
        super(message);
        addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return BAD_REQUEST.value();
    }
}
