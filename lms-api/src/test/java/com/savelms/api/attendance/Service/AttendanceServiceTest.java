package com.savelms.api.attendance.Service;

import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.attendance.domain.entity.Attendance;
import com.savelms.core.attendance.dto.AttendanceDto;
import com.savelms.core.attendance.repository.AttendanceRepository;
import com.savelms.core.attendance.service.AttendanceServiceImpl;
import com.savelms.core.calendar.DayType;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.user.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class AttendanceServiceTest {

    @InjectMocks private AttendanceServiceImpl attendanceService;
    @Mock private AttendanceRepository attendanceRepository;

    @Test
    void 조회_테스트() {
        // Given
        LocalDate date = LocalDate.now();

        given(attendanceRepository.findByUsernameAndDate(anyString(), eq(date)))
                .willReturn(Optional.ofNullable(createAttendance()));

        // When
        AttendanceDto attendanceDto = attendanceService.getAttendanceByDate("test", date);

        // Then
        assertThat(attendanceDto).isNotNull();
        assertThat(attendanceDto.getCheckInStatus()).isEqualTo(AttendanceStatus.NONE);
    }

    public Attendance createAttendance() {
        return Attendance.builder()
                .checkInStatus(AttendanceStatus.NONE)
                .checkOutStatus(AttendanceStatus.NONE)
                .user(createUser())
                .calendar(createCalender())
                .build();
    }

    public User createUser() {
        return User.createDefaultUser("test", "test", "test");
    }

    public Calendar createCalender() {
        return Calendar.builder()
                .date(LocalDate.now())
                .dayType(DayType.STUDYDAY)
                .build();
    }

}
