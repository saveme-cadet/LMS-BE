package com.savelms.core.attendance.dto;

import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.attendance.domain.entity.Attendance;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceDto {

    private String username;
    private AttendanceStatus checkInStatus;
    private AttendanceStatus checkOutStatus;

    public AttendanceDto(Attendance attendance) {
        this.username = attendance.getUser().getUsername();
        this.checkInStatus = attendance.getCheckInStatus();
        this.checkOutStatus = attendance.getCheckOutStatus();
    }

}
