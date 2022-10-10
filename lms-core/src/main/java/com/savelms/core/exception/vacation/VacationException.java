package com.savelms.core.exception.vacation;

import com.savelms.core.exception.ExceptionStatus;
import lombok.Getter;

@Getter
public class VacationException extends RuntimeException {

    private final ExceptionStatus exceptionStatus;

    public VacationException(ExceptionStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
}
