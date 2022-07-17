package com.savelms.core.user.emailauth.domain;

public class EmailAuthTokenNotFoundException extends RuntimeException{

    public EmailAuthTokenNotFoundException() {
        super();
    }

    public EmailAuthTokenNotFoundException(String message) {
        super(message);
    }

    public EmailAuthTokenNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailAuthTokenNotFoundException(Throwable cause) {
        super(cause);
    }
}
