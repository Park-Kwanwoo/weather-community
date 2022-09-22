package org.project.weathercommunity.exception;

public class JwtExpiredException extends WeatherCommunityException {

    private static final String MESSAGE = "만료된 토큰입니다.";
    public JwtExpiredException() {
        super(MESSAGE);
    }

    public JwtExpiredException(Throwable cause) {
        super(MESSAGE, cause);
    }

    // UnAuthorized
    @Override
    public int getStatusCode() {
        return 401;
    }
}
