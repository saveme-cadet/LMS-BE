package com.savelms.core.exception;

public class VacationNotFoundException extends RuntimeException {
    public VacationNotFoundException() {
        super();
    }

    public VacationNotFoundException(String message) {
        super(message);
    }

    public VacationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public VacationNotFoundException(Throwable cause) {
        super(cause);
    }

    protected VacationNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
