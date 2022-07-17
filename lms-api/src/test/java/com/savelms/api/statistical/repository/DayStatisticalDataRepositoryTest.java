package com.savelms.api.statistical.repository;

import com.savelms.api.ApiApplication;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.statistical.DayStatisticalData;
import com.savelms.core.statistical.DayStatisticalDataRepository;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("통계 레포지토리 테스트")
@Transactional
@SpringBootTest(classes = ApiApplication.class)
public class DayStatisticalDataRepositoryTest {

    @Autowired private UserRepository userRepository;
    @Autowired private CalendarRepository calendarRepository;
    @Autowired private DayStatisticalDataRepository statisticalDataRepository;

    Calendar calendar;

    @BeforeEach
    void before() {
        calendar = calendarRepository.findAllByDate(LocalDate.now());
    }

    @DisplayName("유저네임, 생성날짜로 조회 테스트")
    @Test
    void findByUsernameAndDateTest() {
        // Given
        User user1 = createUser("test1");
        User user2 = createUser("test2");
        userRepository.save(user1);
        userRepository.save(user2);

        DayStatisticalData dayStatisticalData1 = createDayStatisticalData(user1, calendar);
        DayStatisticalData dayStatisticalData2 = createDayStatisticalData(user2, calendar);
        statisticalDataRepository.save(dayStatisticalData1);
        statisticalDataRepository.save(dayStatisticalData2);

        // When
        DayStatisticalData findStatisticalData = statisticalDataRepository.findByUsernameAndDate(user1.getUsername(), LocalDate.now())
                .orElseThrow(() -> new EntityNotFoundException("엔티티 없음"));

        // Then
        assertThat(findStatisticalData.getId()).isEqualTo(dayStatisticalData1.getId());
    }

    @DisplayName("api ID, 생성날짜로 조회 테스트")
    @Test
    void findByApiIdAndDateTest() {
        // Given
        User user = createUser("test");
        userRepository.save(user);

        DayStatisticalData dayStatisticalData = createDayStatisticalData(user, calendar);
        statisticalDataRepository.save(dayStatisticalData);

        // When
        DayStatisticalData findStatisticalData = statisticalDataRepository.findByApiIdAndDate(user.getApiId(), LocalDate.now())
                .orElseThrow(() -> new EntityNotFoundException("엔티티 없음"));

        // Then
        assertThat(findStatisticalData.getId()).isEqualTo(dayStatisticalData.getId());
    }

    public User createUser(String username) {
        return User.createDefaultUser(username, "test", "test");
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

}
