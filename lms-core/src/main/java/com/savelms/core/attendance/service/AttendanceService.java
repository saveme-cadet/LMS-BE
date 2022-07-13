package com.savelms.core.attendance.service;

import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.user.domain.entity.User;

public interface AttendanceService {
    void checkIn(Long attendanceId, User user, AttendanceStatus status);

    void checkOut(Long attendanceId, User user, AttendanceStatus status);
}
