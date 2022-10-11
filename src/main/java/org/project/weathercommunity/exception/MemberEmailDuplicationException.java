package org.project.weathercommunity.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class MemberEmailDuplicationException extends WeatherCommunityException {

    private static final String MESSAGE = "사용할 수 없는 이메일입니다.";

    public MemberEmailDuplicationException() {
        super(MESSAGE);
    }

    public MemberEmailDuplicationException(Throwable cause) {
        super(MESSAGE, cause);
    }

    public MemberEmailDuplicationException(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return BAD_REQUEST.value();
    }
}
