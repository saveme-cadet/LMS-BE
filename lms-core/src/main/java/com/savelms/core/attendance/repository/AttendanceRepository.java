package com.savelms.core.attendance.repository;

import com.savelms.core.attendance.domain.entity.Attendance;
import com.savelms.core.calendar.domain.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByCalendar(Calendar calendar);
    Optional<Attendance> findAllByUserIdAndCalendarId(Long userId, Long calId);

    Optional<Attendance> findAllByCalendar(Long calendarId);
    List<Attendance> findAllByUser(Long userId);

}
