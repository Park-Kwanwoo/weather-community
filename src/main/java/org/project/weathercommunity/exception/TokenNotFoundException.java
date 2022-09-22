package org.project.weathercommunity.exception;

public class TokenNotFoundException extends WeatherCommunityException {

    private static final String MESSAGE = "존재하지 않는 토큰입니다.";

    public TokenNotFoundException() {
        super(MESSAGE);
    }

    public TokenNotFoundException(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
