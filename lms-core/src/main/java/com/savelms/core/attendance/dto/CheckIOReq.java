package com.savelms.core.attendance.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.savelms.core.attendance.domain.AttendanceStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class CheckIOReq {

    @NotNull
    AttendanceStatus status;

    @JsonCreator
    public static AttendanceStatus from(String s) {
        return AttendanceStatus.valueOf(s.toUpperCase());
    }
}
