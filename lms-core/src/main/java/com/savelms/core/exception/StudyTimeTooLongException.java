package com.savelms.core.exception;

public class StudyTimeTooLongException extends RuntimeException {
    public StudyTimeTooLongException() {
        super();
    }

    public StudyTimeTooLongException(String message) {
        super(message);
    }

    public StudyTimeTooLongException(String message, Throwable cause) {
        super(message, cause);
    }

    public StudyTimeTooLongException(Throwable cause) {
        super(cause);
    }

    protected StudyTimeTooLongException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
