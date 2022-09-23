package com.savelms.api.attendance.service;

import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.attendance.dto.AttendanceDto;
import com.savelms.core.user.domain.entity.User;

import java.time.LocalDate;
import java.util.Map;

public interface AttendanceService {
    void checkIn(Long attendanceId, String apiId, AttendanceStatus status);
    void checkOut(Long attendanceId, String apiId, AttendanceStatus status);
    AttendanceDto getAttendanceByDate(String username, LocalDate date);

    public Map<Long, AttendanceDto> getAllAttendanceByDate(LocalDate date);
}
