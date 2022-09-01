package com.savelms.core.exception;

public class StudyTimeMeasurementException extends RuntimeException {
    public StudyTimeMeasurementException() {
        super();
    }

    public StudyTimeMeasurementException(String message) {
        super(message);
    }

    public StudyTimeMeasurementException(String message, Throwable cause) {
        super(message, cause);
    }

    public StudyTimeMeasurementException(Throwable cause) {
        super(cause);
    }

    protected StudyTimeMeasurementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
