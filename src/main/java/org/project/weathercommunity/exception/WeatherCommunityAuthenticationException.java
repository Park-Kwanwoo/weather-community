package org.project.weathercommunity.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class WeatherCommunityAuthenticationException extends AuthenticationException {

    private final Map<String, String> validation = new HashMap<>();

    public WeatherCommunityAuthenticationException(String message) {
        super(message);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
