package org.project.weathercommunity.exception;

/**
 * status -> 400
 */
public class InvalidRequestException extends WeatherCommunityException {

    private static final String MESSAGE = "잘못된 요청입니다.";

    public InvalidRequestException() {
        super(MESSAGE);
    }

    public InvalidRequestException(Throwable cause) {
        super(MESSAGE, cause);
    }

    public InvalidRequestException(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
