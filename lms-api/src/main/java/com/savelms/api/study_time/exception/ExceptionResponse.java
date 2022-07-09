package com.savelms.api.study_time.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExceptionResponse {
    private LocalDateTime timeStamp;
    private String message;
    private String exception;
    private String path;
}
