package com.savelms.core.attendance.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum AttendanceStatus {
    NONE("없음", 0.0, 0.0),
    PRESENT("출석", 0.5, 0.0),
    TARDY("지각", 0.0, 0.25),
    ABSENT("결석", 0.0, 0.5),
    OFFICIAL_ABSENT("공결", 0.0, 0.0),
    ILLNESS("병결", 0.0, 0.0),
    VACATION("휴가", 0.0, 0.0);

    private String value;
    private Double attendanceScore;
    private Double attendancePenalty;

    AttendanceStatus(String value, Double attendanceScore, Double attendancePenalty) {
        this.value = value;
        this.attendanceScore = attendanceScore;
        this.attendancePenalty = attendancePenalty;
    }


}