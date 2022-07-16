package com.savelms.api.study_time.repository;

import com.savelms.api.ApiApplication;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.statistical.DayStatisticalData;
import com.savelms.core.study_time.domain.entity.StudyTime;
import com.savelms.core.study_time.domain.repository.StudyTimeRepository;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("스터디 레포지토리 테스트")
@Transactional
@SpringBootTest(classes = ApiApplication.class)
public class StudyTimeRepositoryTest {

    @Autowired private UserRepository userRepository;
    @Autowired private CalendarRepository calendarRepository;
    @Autowired private StudyTimeRepository studyTimeRepository;

    @DisplayName("생성 및 조회")
    @Test
    void studyStartTest() {
        // Given
        User user = createUser();
        userRepository.save(user);

        Calendar calendar = calendarRepository.findAllByDate(LocalDate.now());

        StudyTime studyTime = createStudyTime(user, calendar);

        // When
        studyTimeRepository.save(studyTime);

        StudyTime findStudyTime = studyTimeRepository.findById(studyTime.getId())
                .orElseThrow(() -> new IllegalStateException("엔티티 없음"));

        // Then
        assertThat(findStudyTime).isEqualTo(studyTime);

    }

    @DisplayName("유저네임 and 공부중 조회")
    @Test
    void findByUsernameAndIsStudyingTest() {
        // Given
        User user = createUser();
        userRepository.save(user);

        Calendar calendar = calendarRepository.findAllByDate(LocalDate.now());

        StudyTime studyTime = createStudyTime(user, calendar);
        studyTimeRepository.save(studyTime);

        // When
        StudyTime findStudyTime = studyTimeRepository.findByUsernameAndIsStudying(studyTime.getUser().getUsername(), true)
                .orElseThrow(() -> new IllegalStateException("엔티티 없음"));

        // Then
        assertThat(findStudyTime.getUser().getUsername()).isEqualTo(studyTime.getUser().getUsername());
        assertThat(findStudyTime.getIsStudying()).isEqualTo(true);
    }

    public User createUser() {
        return User.createDefaultUser("test", "test", "test");
    }

    public StudyTime createStudyTime(User user, Calendar calendar) {
        return StudyTime.of(user, calendar);
    }

}
