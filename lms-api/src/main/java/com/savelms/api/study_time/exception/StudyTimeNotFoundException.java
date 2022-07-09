package com.savelms.api.study_time.exception;

public class StudyTimeNotFoundException extends RuntimeException {
    public StudyTimeNotFoundException() {
        super();
    }

    public StudyTimeNotFoundException(String message) {
        super(message);
    }

    public StudyTimeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public StudyTimeNotFoundException(Throwable cause) {
        super(cause);
    }

    protected StudyTimeNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
