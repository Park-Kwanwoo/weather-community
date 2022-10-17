package org.project.weathercommunity.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;
/**
 * status -> 404
 */
public class PostNotFoundException extends WeatherCommunityException {

    private static final String MESSAGE = "존재하지 않는 글입니다.";
    public PostNotFoundException() {
        super(MESSAGE);
    }

    public PostNotFoundException(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getStatusCode() {
        return NOT_FOUND.value();
    }
}
