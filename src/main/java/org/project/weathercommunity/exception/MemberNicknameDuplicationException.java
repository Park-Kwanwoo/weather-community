package org.project.weathercommunity.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class MemberNicknameDuplicationException extends WeatherCommunityException {

    private final static String MESSAGE = "사용할 수 없는 닉네임입니다.";

    public MemberNicknameDuplicationException() {
        super(MESSAGE);
    }

    public MemberNicknameDuplicationException(String field, String message) {
        super(MESSAGE);
        addValidation(field, message);
    }

    @Override
    public int getStatusCode() {
        return BAD_REQUEST.value();
    }
}
