package org.project.weathercommunity.exception;

public class MemberDuplicateException extends WeatherCommunityException {

    private static final String MESSAGE = "중복된 회원입니다.";

    public MemberDuplicateException() {
        super(MESSAGE);
    }

    public MemberDuplicateException(Throwable cause) {
        super(MESSAGE, cause);
    }

    public MemberDuplicateException(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
