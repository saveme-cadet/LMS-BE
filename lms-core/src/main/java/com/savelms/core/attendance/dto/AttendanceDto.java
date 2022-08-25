package com.savelms.core.attendance.dto;

import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.attendance.domain.entity.Attendance;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceDto {

    private String userId;
    private AttendanceStatus checkInStatus;
    private AttendanceStatus checkOutStatus;

    public AttendanceDto(Attendance attendance) {
        this.userId = attendance.getUser().getApiId();
        this.checkInStatus = attendance.getCheckInStatus();
        this.checkOutStatus = attendance.getCheckOutStatus();
    }

}
