package com.savelms.api.attendance.controller;

import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.attendance.domain.entity.Attendance;
import com.savelms.core.attendance.repository.AttendanceRepository;
import com.savelms.api.attendance.service.AttendanceService;
import com.savelms.core.calendar.DayType;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.exception.NoPermissionException;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Disabled
@SpringBootTest
@Transactional
class AttendanceControllerTest {

    @Autowired
    AttendanceService attendanceService;
    @Autowired
    AttendanceRepository attendanceRepository;
    @Autowired
    CalendarRepository calendarRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager em;

    /**
     * Service 유닛테스트 -> core 모듈에서는 빈 주입이 안되어서 api 모듈에서 테스트함
     */
    @Test
    void 본인_당일_정상_체크인() throws Exception{
        //given
        User testUser = userRepository.findByUsername("user").get();

        Calendar today = Calendar.builder()
                .date(LocalDate.now())
                .dayType(DayType.STUDYDAY)
                .build();

        Attendance todayAttendance = Attendance.createAttendance(testUser, today);

        userRepository.save(testUser);
        calendarRepository.save(today);
        attendanceRepository.save(todayAttendance);

        em.flush();
        em.clear();

        //when
        attendanceService.checkIn(todayAttendance.getId(), testUser, AttendanceStatus.PRESENT);

        //then
        Attendance findAttendance = attendanceRepository.findById(todayAttendance.getId()).get();
        Assertions.assertThat(findAttendance.getCheckInStatus()).isEqualTo(AttendanceStatus.PRESENT);
    }

    @Test
    void 지난날짜_체크인() throws Exception{
        //given
        User testUser = userRepository.findByUsername("user").get();

        Calendar yesterday = Calendar.builder()
                .date(LocalDate.now().minusDays(1))
                .dayType(DayType.STUDYDAY)
                .build();

        Attendance yesterdayAttendance = Attendance.createAttendance(testUser, yesterday);

        userRepository.save(testUser);
        calendarRepository.save(yesterday);
        attendanceRepository.save(yesterdayAttendance);

        em.flush();
        em.clear();

        //when, then
        Assertions.assertThatThrownBy(
                        () -> attendanceService.checkIn(yesterdayAttendance.getId(), testUser, AttendanceStatus.PRESENT))
                .isInstanceOf(NoPermissionException.class);
    }

    @Test
    void 권한없는_체크인() throws Exception{
        //given
        User testUser = userRepository.findByUsername("user").get();

        /**
         * 중요!
         * user1은 기본 생성되지 않습니다. 제 로컬 테스트 환경에서는 DB에서 데이터 삽입해서 테스트 했습니다.
         */
        User wrongUser = userRepository.findByUsername("user1").get();

        Calendar today = Calendar.builder()
                .date(LocalDate.now())
                .dayType(DayType.STUDYDAY)
                .build();

        Attendance todayAttendance = Attendance.createAttendance(testUser, today);

        userRepository.save(testUser);
        userRepository.save(wrongUser);
        calendarRepository.save(today);
        attendanceRepository.save(todayAttendance);

        em.flush();
        em.clear();

        //when, then
        Assertions.assertThatThrownBy(
                        () -> attendanceService.checkIn(todayAttendance.getId(), wrongUser, AttendanceStatus.PRESENT))
                .isInstanceOf(NoPermissionException.class);
    }

    @Test
    void 머슴이_다른유저_체크인() throws Exception{
        //given
        User testUser = userRepository.findByUsername("user").get();

        Calendar today = Calendar.builder()
                .date(LocalDate.now())
                .dayType(DayType.STUDYDAY)
                .build();

        Attendance todayAttendance = Attendance.createAttendance(testUser, today);

        userRepository.save(testUser);
        calendarRepository.save(today);
        attendanceRepository.save(todayAttendance);

        em.flush();
        em.clear();

        //when
        User manager = userRepository.findByUsername("manager").get();
        attendanceService.checkIn(todayAttendance.getId(), manager, AttendanceStatus.PRESENT);

        em.flush();
        em.clear();

        //then
        Attendance findAttendance = attendanceRepository.findById(todayAttendance.getId()).get();
        Assertions.assertThat(findAttendance.getCheckInStatus()).isEqualTo(AttendanceStatus.PRESENT);
    }

    @Test
    void 체크인_하지않고_체크아웃() throws Exception{
        //given
        User testUser = userRepository.findByUsername("user").get();

        Calendar today = Calendar.builder()
                .date(LocalDate.now())
                .dayType(DayType.STUDYDAY)
                .build();

        Attendance todayAttendance = Attendance.createAttendance(testUser, today);

        userRepository.save(testUser);
        calendarRepository.save(today);
        attendanceRepository.save(todayAttendance);

        em.flush();
        em.clear();

        //when, then
        Assertions.assertThatThrownBy(
                        () -> attendanceService.checkOut(todayAttendance.getId(), testUser, AttendanceStatus.PRESENT))
                .isInstanceOf(IllegalStateException.class);
    }

    /**
     * 필요하다면 캘린더 생성 용 로직으로 사용해도 될 것 같습니다.
     */
    private void 캘린더_생성_7월() {

        LocalDate startDate = LocalDate.of(2022, 7, 1);
        LocalDate endDate = LocalDate.of(2022, 8, 1);

        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            Calendar calendar = Calendar.builder()
                    .date(date)
                    .dayType( date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                            date.getDayOfWeek() == DayOfWeek.SUNDAY ?
                            DayType.HOLIDAY : DayType.STUDYDAY)
                    .build();
            calendarRepository.save(calendar);
        }
    }

    /**
     * 필요하다면 출결 생성 용 로직으로 사용해도 될 것 같습니다.
     * 단, 유저와 캘린더가 반드시 먼저 생성되어 있어야 합니다.
     */
    private void 출결_생성() {

        User user = userRepository.findByUsername("user").get();
        User manager = userRepository.findByUsername("manager").get();

        LocalDate startDate = LocalDate.of(2022, 7, 1);
        LocalDate endDate = LocalDate.now().plusDays(1);

        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            Calendar calendar = calendarRepository.findByDate(date).get();

            //공부하는 날만 생성하기 보다는,
            //모든날에 다 생성하면 일일 통계낼때
            //더 편리할 것 같다. -> 날짜 이동
//            if (calendar.getDayType().equals(DayType.STUDYDAY)) {
            Attendance userAttendance = Attendance.builder()
                    .calendar(calendar)
                    .user(user)
                    .build();
            attendanceRepository.save(userAttendance);
//            }

            //편의를 위해 일반 유저(카뎃)의 출결만 생성
            //아래코드는 매니저 유저(머슴)의 츌결도 생성
            //위 유저들은 기본 생성되는 테스트 유저를 의미함
//            Attendance managerAttendance = Attendance.builder()
//                    .calendar(calendar)
//                    .user(manager)
//                    .build();
//            attendanceRepository.save(managerAttendance);

        }
    }
}