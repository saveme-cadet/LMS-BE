package com.savelms.core.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {

    ENTITY_NOT_FOUND("E1001"),
    ENTITY_DUPLICATION("E1002"),

    USER_NOT_FOUND("U2001"),
    USER_DUPLICATION_FOUND("U2002"),

    STUDY_TIME_NOT_FOUND("S5001"),
    STUDY_TIME_DUPLICATION("S5002"),
    STUDY_TIME_UPDATE_BEGIN_TIME("S5003"),
    STUDY_TIME_UPDATE_END_TIME("S5004"),
    STUDY_TIME_OVER_24_HOURS("S5005"),
    STUDY_TIME_END_LESS_THEN_BEGIN("S5006"),

    VACATION_NOT_ENOUGH("V6002"),

    BAD_REQUEST("B8001"),
    INVALID_REQUEST_BODY("B8002");

    final String code;

    ExceptionCode(String code) {
        this.code = code;
    }
}
