package org.project.weathercommunity.exception;

/**
 * status -> 404
 */

public class MemberNotFoundException extends WeatherCommunityException {

    private static final String MESSAGE = "존재하지 않는 회원입니다.";

    public MemberNotFoundException() {
        super(MESSAGE);
    }

    public MemberNotFoundException(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
