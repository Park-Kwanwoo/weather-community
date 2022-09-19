package org.project.weathercommunity.exception;

public class JwtExpiredException extends WeatherCommunityException {

    public JwtExpiredException(String message) {
        super(message);
    }

    public JwtExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    // UnAuthorized
    @Override
    public int getStatusCode() {
        return 401;
    }
}
