package com.savelms.core.attendance.service;

import com.savelms.core.attendance.domain.entity.Attendance;
import com.savelms.core.attendance.repository.AttendanceRepository;
import com.savelms.core.calendar.DayType;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.user.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AttendanceServiceImplTest.class})
class AttendanceServiceImplTest {

    @Autowired
    AttendanceService attendanceService;
    @PersistenceContext
    EntityManager em;

    @Test
    void 정상_체크인() throws Exception{
        //given
        User user = User.createDefaultUser(
                "test1", "password", "test@email.com");
        em.persist(user);

        Calendar calendar = Calendar.builder()
                .date(LocalDate.now())
                .dayType(DayType.STUDYDAY)
                .build();
        em.persist(calendar);

        Attendance.createAttendance(user, calendar);

        //when

        //then
    }

    @Test
    void 지난날짜_체크인() throws Exception{
        //given

        //when

        //then
    }

    @Test
    void 권한없는_체크인() throws Exception{
        //given

        //when

        //then
    }

    @Test
    void 머슴이_다른유저_체크인() throws Exception{
        //given

        //when

        //then
    }

    @TestConfiguration
    class Config {

    }
}