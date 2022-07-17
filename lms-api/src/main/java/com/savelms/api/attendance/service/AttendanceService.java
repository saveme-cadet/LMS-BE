package com.savelms.api.attendance.service;

import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.attendance.dto.AttendanceDto;
import com.savelms.core.user.domain.entity.User;

import java.time.LocalDate;

public interface AttendanceService {
    void checkIn(Long attendanceId, User user, AttendanceStatus status);
    void checkOut(Long attendanceId, User user, AttendanceStatus status);
    AttendanceDto getAttendanceByDate(String username, LocalDate date);
}
