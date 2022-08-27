package com.savelms.core.attendance.dto;

import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.attendance.domain.entity.Attendance;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AttendanceDto {

    private String userId;
    private Long attendanceId;
    private AttendanceStatus checkInStatus;
    private AttendanceStatus checkOutStatus;

    public AttendanceDto(Attendance attendance) {
        this.userId = attendance.getUser().getApiId();
        this.attendanceId = attendance.getId();
        this.checkInStatus = attendance.getCheckInStatus();
        this.checkOutStatus = attendance.getCheckOutStatus();
    }
}
