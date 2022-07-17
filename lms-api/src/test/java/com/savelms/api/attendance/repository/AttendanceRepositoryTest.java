package com.savelms.api.attendance.repository;

import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.attendance.domain.entity.Attendance;
import com.savelms.core.attendance.repository.AttendanceRepository;
import com.savelms.core.calendar.DayType;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class AttendanceRepositoryTest {

    @Autowired private UserRepository userRepository;
    @Autowired private CalendarRepository calendarRepository;
    @Autowired private AttendanceRepository attendanceRepository;

    @Test
    void 조회_테스트() {
        // Given
        User user = createUser();
        userRepository.save(user);

        Calendar calendar = calendarRepository.findByDate(LocalDate.now()).get();

        Attendance attendance = createAttendance(user, calendar);
        attendanceRepository.save(attendance);

        // When
        Attendance findAttendance = attendanceRepository.findByUsernameAndDate("test", LocalDate.now())
                .orElseThrow(() -> new EntityNotFoundException("엔티티 없음"));

        // Then
        assertThat(findAttendance.getCheckInStatus()).isEqualTo(attendance.getCheckInStatus());
        assertThat(findAttendance.getUser().getUsername()).isEqualTo(attendance.getUser().getUsername());
    }

    public Attendance createAttendance(User user, Calendar calendar) {
        return Attendance.builder()
                .checkInStatus(AttendanceStatus.NONE)
                .checkOutStatus(AttendanceStatus.NONE)
                .user(user)
                .calendar(calendar)
                .build();
    }

    public User createUser() {
        return User.createDefaultUser("test", "test", "test");
    }

}
