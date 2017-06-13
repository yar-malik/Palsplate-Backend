package com.jersey.Authorization;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by muhammad on 6/13/17.
 */
public class UserNotActivatedException extends AuthenticationException {

    public UserNotActivatedException(String msg, Throwable t) {
        super(msg, t);
    }

    public UserNotActivatedException(String msg) {
        super(msg);
    }
}
