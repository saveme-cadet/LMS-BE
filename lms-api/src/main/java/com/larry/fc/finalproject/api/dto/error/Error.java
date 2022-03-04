package com.larry.fc.finalproject.api.dto.error;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Error {
    private String field;
    private String message;
    private String invalidValue;
}
