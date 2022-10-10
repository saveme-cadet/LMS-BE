package com.savelms.core.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FieldExceptionResponse extends ExceptionResponse {

    private final List<FieldError> fieldErrors;

    private FieldExceptionResponse(ExceptionStatus status, List<FieldError> fieldErrors) {
        super(status);
        this.fieldErrors = fieldErrors;
    }

    public static FieldExceptionResponse of(ExceptionStatus status, BindingResult bindingResult) {
        return new FieldExceptionResponse(status, FieldError.of(bindingResult));
    }

    @Getter
    public static class FieldError {

        private final String field;
        private final String value;
        private final String reason;

        private FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldError> of(BindingResult bindingResult) {
            return bindingResult.getFieldErrors().stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()
                    ))
                    .collect(Collectors.toUnmodifiableList());
        }
    }
}
