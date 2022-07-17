package com.savelms.api.study_time.service;

import com.savelms.api.statistical.service.DayStatisticalDataService;
import com.savelms.api.study_time.dto.StudyTimeResponse;
import com.savelms.core.calendar.DayType;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.study_time.domain.entity.StudyTime;
import com.savelms.core.study_time.domain.repository.StudyTimeRepository;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Disabled
@ExtendWith(MockitoExtension.class)
public class StudyTimeServiceTest {

    @InjectMocks private StudyTimeService studyTimeService;

    @Mock private UserRepository userRepository;
    @Mock private CalendarRepository calendarRepository;
    @Mock private StudyTimeRepository studyTimeRepository;

    @Test
    void test() {
        // Given
        User user1 = createUser("user1");
        Calendar calender = createCalender();
        StudyTime studyTime = createStudyTime(user1, calender);

        ArgumentCaptor<StudyTime> captor = ArgumentCaptor.forClass(StudyTime.class);

        given(userRepository.findByUsername(anyString())).willReturn(Optional.ofNullable(user1));
        given(calendarRepository.findAllByDate(LocalDate.now())).willReturn(calender);

        // When
        StudyTimeResponse studyTimeResponse = studyTimeService.startStudy(user1.getUsername());

        // Then
        verify(studyTimeRepository, times(1)).save(captor.capture());
        StudyTime findStudyTime = captor.getValue();
    }

    public User createUser(String username) {
        return User.createDefaultUser(username, "test", "test");
    }

    public Calendar createCalender() {
        return Calendar.builder()
                .date(LocalDate.now())
                .dayType(DayType.STUDYDAY)
                .build();
    }

    public StudyTime createStudyTime(User user, Calendar calendar) {
        return StudyTime.of(user, calendar);
    }

    @EnableJpaAuditing
    @TestConfiguration //테스트할 때만 실행되는 설정빈
    public static class TestJpaConfig {
        @Bean
        public AuditorAware<String> auditorAware() {
            return () -> Optional.of("uno");
        }
    }

}
