package com.savelms.core.attendance.domain.repository;

import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.attendance.domain.entity.Attendance;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.user.AttendStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByCalendar(Calendar calendar);
    Optional<Attendance> findAllByUserIdAndCalendarId(Long userId, Long calId);

    Optional<Attendance> findAllByCalendarId(Long calendarId);
    List<Attendance> findAllByUserId(Long userId);

    Stream<Attendance> findAttendanceByUserId(Long userId);

    List<Attendance> findFirstById(Long attendanceId);


//    @Query("select distinct u FROM Attendance u join fetch u.user ")
    Optional<Attendance> findAttendanceById(Long attendanceId);

    @Query("select a from Attendance a join fetch a.user u " +
            "where a.user.username =:username and a.calendar.date =:date")
    Optional<Attendance> findByUsernameAndDate(@Param("username") String username, @Param("date") LocalDate date);

    @Query("select distinct a from Attendance a " +
            "join fetch a.user u " +
            "join fetch u.userRoles ur " +
            "join fetch ur.role r " +
        "where a.calendar.date =:date")
    List<Attendance> findAllByDateWithUser(@Param("date") LocalDate date);

    @Query("select distinct a from Attendance a " +
            "join fetch a.user u " +
            "join fetch u.userRoles ur " +
            "join fetch ur.role r " +
            "where a.calendar.date = :date and u.attendStatus = :attendStatus")
    List<Attendance> findAllByDateAndAttendStatusWithUser(
            @Param("date") LocalDate date,
            @Param("attendStatus") AttendStatus attendStatus);

}
