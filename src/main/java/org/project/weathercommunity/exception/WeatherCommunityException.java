package org.project.weathercommunity.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class WeatherCommunityException extends RuntimeException {

    private final Map<String, String> validation = new HashMap<>();
    public WeatherCommunityException(String message) {
        super(message);
    }

    public WeatherCommunityException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
