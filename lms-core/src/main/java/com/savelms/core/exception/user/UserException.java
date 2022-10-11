package com.savelms.core.exception.user;

import com.savelms.core.exception.ExceptionStatus;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

    private final ExceptionStatus exceptionStatus;

    public UserException(ExceptionStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
}
