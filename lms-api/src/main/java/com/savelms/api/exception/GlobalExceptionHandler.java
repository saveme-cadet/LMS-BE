package com.savelms.api.exception;

import com.savelms.core.exception.ExceptionResponse;
import com.savelms.core.exception.ExceptionStatus;
import com.savelms.core.exception.FieldExceptionResponse;
import com.savelms.core.exception.study_time.StudyTimeException;
import com.savelms.core.exception.user.UserException;
import com.savelms.core.exception.vacation.VacationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        log.error(e.getClass().getName(), e.getMessage(), ExceptionStatus.INVALID_REQUEST_BODY.getMessage());

        ExceptionResponse response = FieldExceptionResponse.of(ExceptionStatus.INVALID_REQUEST_BODY, e.getBindingResult());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ExceptionResponse> handleUserException(UserException e) {
        log.error(e.getClass().getName(), e.getMessage());

        ExceptionResponse exceptionResponse = ExceptionResponse.from(e.getExceptionStatus());

        return new ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.valueOf(exceptionResponse.getStatus()));
    }

    @ExceptionHandler(StudyTimeException.class)
    public ResponseEntity<ExceptionResponse> handleStudyTimeException(StudyTimeException e) {
        log.error(e.getClass().getName(), e.getMessage());

        ExceptionResponse exceptionResponse = ExceptionResponse.from(e.getExceptionStatus());

        return new ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.valueOf(exceptionResponse.getStatus()));
    }

    @ExceptionHandler(VacationException.class)
    public ResponseEntity<ExceptionResponse> handleVacationException(VacationException e) {
        log.error(e.getClass().getName(), e.getMessage());

        ExceptionResponse exceptionResponse = ExceptionResponse.from(e.getExceptionStatus());

        return new ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.valueOf(exceptionResponse.getStatus()));
    }
}
