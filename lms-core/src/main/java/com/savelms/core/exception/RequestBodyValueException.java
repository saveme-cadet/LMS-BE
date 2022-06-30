package com.savelms.core.exception;

public class RequestBodyValueException extends RuntimeException {

    public RequestBodyValueException() {
        super();
    }

    public RequestBodyValueException(String message) {
        super(message);
    }

    public RequestBodyValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestBodyValueException(Throwable cause) {
        super(cause);
    }
}
