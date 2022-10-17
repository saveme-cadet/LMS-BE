package com.savelms.core.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum ExceptionStatus {

    EMAIL_NOT_FOUND(400, ExceptionCode.ENTITY_NOT_FOUND, "존재하지 않는 이메일입니다."),
    PASSWORD_NOT_FOUND(400, ExceptionCode.ENTITY_NOT_FOUND,"패스워드를 잘못 입력하셨습니다."),
    USER_NOT_FOUND(400, ExceptionCode.ENTITY_NOT_FOUND, "존재하지 않는 회원입니다."),
    VACATION_NOT_FOUND(400, ExceptionCode.ENTITY_NOT_FOUND, "존재하지 않는 휴가 기록입니다."),
    STUDY_TIME_NOT_FOUND(400, ExceptionCode.STUDY_TIME_NOT_FOUND, "존재하지 않는 아오지 기록입니다."),

    EMAIL_DUPLICATION(400, ExceptionCode.ENTITY_DUPLICATION,"이미 존재하는 이메일입니다."),
    NICKNAME_DUPLICATION(400, ExceptionCode.ENTITY_DUPLICATION,"이미 존재하는 닉네임입니다."),
    STUDY_TIME_DUPLICATION(400, ExceptionCode.STUDY_TIME_DUPLICATION,"지정한 공부시간 범위가 중복됩니다."),

    STUDY_TIME_DELETE(400, ExceptionCode.STUDY_TIME_UPDATE_BEGIN_TIME,"당일 스터디만 삭제할 수 있습니다."),
    STUDY_TIME_UPDATE_BEGIN_TIME(400, ExceptionCode.STUDY_TIME_UPDATE_BEGIN_TIME,"요청한 시작시간이 유효범위를 넘었습니다."),
    STUDY_TIME_UPDATE_END_TIME(400, ExceptionCode.STUDY_TIME_UPDATE_END_TIME,"요청한 종료시간이 유효범위를 넘었습니다."),
    STUDY_TIME_OVER_24_HOURS(400, ExceptionCode.STUDY_TIME_OVER_24_HOURS,"스터디 시간은 24시간 이상 넘어가면 측정이 불가능합니다."),
    STUDY_TIME_END_LESS_THEN_BEGIN(400, ExceptionCode.STUDY_TIME_END_LESS_THEN_BEGIN, "종료시간이 시작시간보다 작을 수 없습니다."),

    VACATION_NOT_ENOUGH(400, ExceptionCode.VACATION_NOT_ENOUGH, "휴가가 부족합니다."),

    BAD_REQUEST(400, ExceptionCode.BAD_REQUEST,"잘못된 접근입니다"),
    INVALID_REQUEST_BODY(400, ExceptionCode.INVALID_REQUEST_BODY,"HTTP 리퀘스트 바디 유효성 체크 에러");

    private final int status;
    private final ExceptionCode code;

    @Setter private String message;

    ExceptionStatus(int status, ExceptionCode code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
