package org.project.weathercommunity.exception;

public class PasswordNotMatchException extends WeatherCommunityException {

    private static final String MESSAGE = "비밀번호가 일치하지 않습니다.";

    public PasswordNotMatchException() {
        super(MESSAGE);
    }

    public PasswordNotMatchException(Throwable cause) {
        super(MESSAGE, cause);
    }

    public PasswordNotMatchException(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
