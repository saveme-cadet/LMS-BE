package com.savelms.core.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExceptionResponse {

    private int status;
    private String code;
    private String message;
    private LocalDateTime timeStamp;

    protected ExceptionResponse() {}

    private ExceptionResponse(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.timeStamp = LocalDateTime.now();
    }

    protected ExceptionResponse(ExceptionStatus status) {
        this.status = status.getStatus();
        this.code = status.getCode().getCode();
        this.message = status.getMessage();
        this.timeStamp = LocalDateTime.now();
    }

    public static ExceptionResponse from(ExceptionStatus status) {
        return new ExceptionResponse(status.getStatus(), status.getCode().getCode(), status.getMessage());
    }
}
