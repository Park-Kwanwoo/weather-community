package org.project.weathercommunity.exception;

import org.springframework.security.core.AuthenticationException;

public class MemberNotFoundAuthenticationException extends AuthenticationException {

    public MemberNotFoundAuthenticationException(String msg) {
        super(msg);
    }
}
