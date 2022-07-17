package com.savelms.api.statistical.service;

import com.savelms.api.ApiApplication;
import com.savelms.api.attendance.service.AttendanceService;
import com.savelms.api.study_time.dto.StudyTimeResponse;
import com.savelms.api.study_time.service.StudyTimeService;
import com.savelms.api.user.role.service.RoleService;
import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.attendance.domain.entity.Attendance;
import com.savelms.core.attendance.repository.AttendanceRepository;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.statistical.DayStatisticalData;
import com.savelms.core.statistical.DayStatisticalDataRepository;
import com.savelms.core.study_time.domain.entity.StudyTime;
import com.savelms.core.study_time.domain.repository.StudyTimeRepository;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
import com.savelms.core.user.role.RoleEnum;
import com.savelms.core.user.role.domain.entity.Role;
import com.savelms.core.user.role.domain.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("통계 서비스 테스트")
@Transactional
@SpringBootTest(classes = ApiApplication.class)
public class DayStatisticalDataServiceTest {

    @Autowired private UserRepository userRepository;
    @Autowired private CalendarRepository calendarRepository;
    @Autowired private StudyTimeRepository studyTimeRepository;
    @Autowired private AttendanceRepository attendanceRepository;
    @Autowired private DayStatisticalDataRepository statisticalDataRepository;

    @Autowired private RoleService roleService;
    @Autowired private StudyTimeService studyTimeService;
    @Autowired private DayStatisticalDataService statisticalDataService;
    @Autowired private AttendanceService attendanceService;

    Calendar calendar;

    @BeforeEach
    void before() {
        calendar = calendarRepository.findAllByDate(LocalDate.now());
    }

    @Test
    void updateStudyTimeScore() {
        // Given
        User user = createUser("test");
        userRepository.save(user);

        StudyTime studyTime = createStudyTime(user, calendar);
        studyTimeRepository.save(studyTime);

        DayStatisticalData dayStatisticalData = createDayStatisticalData(user, calendar);
        statisticalDataRepository.save(dayStatisticalData);

        // When
        statisticalDataService.updateStudyTimeScore(user.getUsername(), 3D, LocalDate.now());

        // Then
        assertThat(dayStatisticalData.getStudyTimeScore()).isEqualTo(3D);
    }

    @DisplayName("StudyTimeService의 endStudy() 함수 호출 시 통계 테이블 변경되는지 확인")
    @Test
    void updateStudyTimeScoreByStudyService() {
        // Given
        User user = createUser("test");
        userRepository.save(user);

        studyTimeService.startStudy(user.getUsername());

        DayStatisticalData dayStatisticalData = createDayStatisticalData(user, calendar);
        statisticalDataRepository.save(dayStatisticalData);


        // When
        StudyTimeResponse studyTimeResponse = studyTimeService.endStudy(user.getUsername());

        // Then
        assertThat(dayStatisticalData.getStudyTimeScore()).isEqualTo(studyTimeResponse.getStudyTimeScore());
    }

    @Test
    void updateAttendance() {
        // Given
        User user = createUser("test");
        userRepository.save(user);

        Attendance attendance = createAttendance(user, calendar);
        attendanceRepository.save(attendance);

        DayStatisticalData dayStatisticalData = createDayStatisticalData(user, calendar);
        statisticalDataRepository.save(dayStatisticalData);

        // When
        attendanceService.checkIn(attendance.getId(), user, AttendanceStatus.PRESENT);

        // Then
        assertThat(dayStatisticalData.getAttendanceScore()).isEqualTo(0.5D);
    }


    public User createUser(String username) {
        User defaultUser = User.createDefaultUser(username, "test", "test");
        Role defaultRole = roleService.findByValue(RoleEnum.ROLE_UNAUTHORIZED);
        UserRole.createUserRole(defaultUser, defaultRole, "signUpDefault", true);

        return defaultUser;
    }

    public StudyTime createStudyTime(User user, Calendar calendar) {
        return StudyTime.of(user, calendar);
    }

    public DayStatisticalData createDayStatisticalData(User user, Calendar calendar) {
        return DayStatisticalData.builder()
                .studyTimeScore(0D)
                .absentScore(0D)
                .attendanceScore(0D)
                .todoSuccessRate(0D)
                .totalScore(0D)
                .weekAbsentScore(0D)
                .user(user)
                .calendar(calendar)
                .build();
    }

    public Attendance createAttendance(User user, Calendar calendar) {
        return Attendance.builder()
                .checkInStatus(AttendanceStatus.NONE)
                .checkOutStatus(AttendanceStatus.NONE)
                .user(user)
                .calendar(calendar)
                .build();
    }

}
