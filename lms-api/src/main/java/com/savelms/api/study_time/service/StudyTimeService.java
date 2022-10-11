package com.savelms.api.study_time.service;

import com.savelms.api.statistical.service.DayStatisticalDataService;
import com.savelms.api.study_time.dto.StudyTimeResponse;
import com.savelms.api.study_time.dto.StudyingUserResponse;
import com.savelms.api.study_time.dto.UpdateStudyTimeRequest;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.exception.ExceptionStatus;
import com.savelms.core.exception.study_time.StudyTimeException;
import com.savelms.core.exception.user.UserException;
import com.savelms.core.study_time.domain.entity.StudyTime;
import com.savelms.core.study_time.domain.repository.StudyTimeRepository;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class StudyTimeService {

    private final UserRepository userRepository;
    private final StudyTimeRepository studyTimeRepository;
    private final CalendarRepository calendarRepository;
    private final DayStatisticalDataService dayStatDataService;

    public StudyTimeResponse createStudyTime(String apiId) {
        User user = userRepository.findByApiId(apiId)
                .orElseThrow(() -> new UserException(ExceptionStatus.USER_NOT_FOUND));
        Calendar calendar = calendarRepository.findAllByDate(LocalDate.now());

        StudyTime studyTime = StudyTime.of(user, calendar);
        studyTimeRepository.save(studyTime);
        
        return StudyTimeResponse.from(studyTime);
    }

    public StudyTime createStudyTime(User user, LocalDateTime beginTime, LocalDateTime endTime) {
        Calendar calendar = calendarRepository.findAllByDate(endTime.toLocalDate());

        StudyTime studyTime = StudyTime.of(user, calendar, beginTime, endTime);
        studyTimeRepository.save(studyTime);

        return studyTime;
    }


    @Transactional(readOnly = true)
    public List<StudyTimeResponse> getStudyTimes(String apiId) {
        List<StudyTime> studyTimes = studyTimeRepository.findByUserApiId(apiId);

        return studyTimes.stream()
                .map(StudyTimeResponse::from)
                .sorted(Comparator.comparing(StudyTimeResponse::getBeginTime))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudyTimeResponse> getTodayStudyTimes(String apiId) {
        List<StudyTime> studyTimes = studyTimeRepository.findByUserApiIdAndDate(apiId, LocalDate.now());

        return studyTimes.stream()
                .map(StudyTimeResponse::from)
                .sorted(Comparator.comparing(StudyTimeResponse::getBeginTime))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudyTimeResponse> getStudyTimesByDate(String apiId, LocalDate date) {
        List<StudyTime> studyTimes = studyTimeRepository.findByUserApiIdAndDate(apiId, date);

        return studyTimes.stream()
                .map(StudyTimeResponse::from)
                .sorted(Comparator.comparing(StudyTimeResponse::getBeginTime))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudyingUserResponse> getStudyingUser(String userId) {
        List<StudyTime> studyTimes = studyTimeRepository.findByIsStudying(userId, true);

        return studyTimes.stream()
                .map(StudyingUserResponse::new)
                .sorted(Comparator.comparing(StudyingUserResponse::getBeginTime))
                .collect(Collectors.toList());
    }


    public StudyTimeResponse endStudy(String apiId) {
        List<StudyTime> studyTimes = studyTimeRepository.findByUserApiIdAndIsStudying(apiId, true);

        if (studyTimes.isEmpty()) {
            throw new StudyTimeException(ExceptionStatus.STUDY_TIME_NOT_FOUND);
        }

        StudyTime studyTime = studyTimes.get(0);
        studyTime.endStudyTime();

        Double studyScore = StudyTime.getStudyScore(studyTime.getBeginTime(), studyTime.getEndTime());
        dayStatDataService.updateStudyTimeScore(apiId, LocalDate.now(), studyScore);

        return StudyTimeResponse.from(studyTime);
    }

    public StudyTimeResponse updateStudyTime(String apiId, Long studyTimeId, UpdateStudyTimeRequest request) {
        StudyTime curStudyTime = studyTimeRepository.findById(studyTimeId)
                .orElseThrow(() -> new StudyTimeException(ExceptionStatus.STUDY_TIME_NOT_FOUND));

        validateStudyTime(curStudyTime.getBeginTime(), curStudyTime.getEndTime(), request.getBeginTime(), request.getEndTime());
        validateDuplicationStudyTime(apiId, curStudyTime.getId(), request.getBeginTime(), request.getEndTime());

        Double oldStudyScore = curStudyTime.getStudyScore();
        Double newStudyScore = StudyTime.getStudyScore(request.getBeginTime(), request.getEndTime()) - oldStudyScore;
        LocalDateTime curStudyNewBeginTime = request.getBeginTime();

        if (request.getBeginTime().toLocalDate().isBefore(curStudyTime.getBeginTime().toLocalDate())) {
            curStudyNewBeginTime = LocalDateTime.of(curStudyTime.getBeginTime().toLocalDate(), LocalTime.MIN);

            StudyTime newStudyTime = createStudyTime(
                    curStudyTime.getUser(),
                    request.getBeginTime(),
                    LocalDateTime.of(request.getBeginTime().toLocalDate(), LocalTime.of(23, 59, 59)));
            dayStatDataService.updateStudyTimeScore(apiId, newStudyTime.getBeginTime().toLocalDate(), newStudyTime.getStudyScore());
        }

        curStudyTime.updateStudyTime(curStudyNewBeginTime, request.getEndTime());
        dayStatDataService.updateStudyTimeScore(apiId, curStudyTime.getEndTime().toLocalDate(), newStudyScore);

        return StudyTimeResponse.from(curStudyTime);
    }

    private void validateStudyTime(LocalDateTime curBeginTime, LocalDateTime curEndTime, LocalDateTime requestedBeginTime, LocalDateTime requestedEndTime) {
        LocalDateTime beginMinDay = curBeginTime.minusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime beginMaxDay = curBeginTime.withHour(23).withMinute(59).withSecond(59);
        LocalDateTime endMinDay = curEndTime.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endMaxDay = curEndTime.withHour(23).withMinute(59).withSecond(59);

        if (requestedBeginTime.isBefore(beginMinDay) || requestedBeginTime.isAfter(beginMaxDay)) {
            throw new StudyTimeException(ExceptionStatus.STUDY_TIME_UPDATE_BEGIN_TIME);
        } else if (requestedEndTime.isBefore(endMinDay) || requestedEndTime.isAfter(endMaxDay)) {
            throw new StudyTimeException(ExceptionStatus.STUDY_TIME_UPDATE_END_TIME);
        }
    }

    private void validateDuplicationStudyTime(String apiId, Long studyTimeId, LocalDateTime beginTime, LocalDateTime endTime) {
        List<StudyTime> studyTimes = studyTimeRepository.findByUserApiIdAndBeginTimeAndEndTimeBetween(apiId, beginTime, endTime).stream()
                .filter(s -> !s.getId().equals(studyTimeId))
                .collect(Collectors.toUnmodifiableList());

        if (!studyTimes.isEmpty()) {
            throw new StudyTimeException(ExceptionStatus.STUDY_TIME_DUPLICATION);
        }
    }


    public void deleteStudyTime(String apiId, Long studyTimeId) {
        StudyTime studyTime = studyTimeRepository.findById(studyTimeId)
                .orElseThrow(() -> new StudyTimeException(ExceptionStatus.STUDY_TIME_NOT_FOUND));

        if (!studyTime.getEndTime().toLocalDate().isEqual(LocalDate.now())) {
            throw new StudyTimeException(ExceptionStatus.STUDY_TIME_DELETE);
        }

        dayStatDataService.updateStudyTimeScore(apiId, studyTime.getEndTime().toLocalDate(), studyTime.getStudyScore() * -1);
        studyTimeRepository.delete(studyTime);
    }
}
