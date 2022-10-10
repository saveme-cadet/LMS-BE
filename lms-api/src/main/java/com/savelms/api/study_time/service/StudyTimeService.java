package com.savelms.api.study_time.service;

import com.savelms.api.statistical.service.DayStatisticalDataService;
import com.savelms.api.study_time.dto.StudyTimeResponse;
import com.savelms.api.study_time.dto.StudyingUserResponse;
import com.savelms.api.study_time.dto.UpdateStudyTimeRequest;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.exception.StudyTimeMeasurementException;
import com.savelms.core.exception.StudyTimeNotFoundException;
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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
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
        List<StudyTime> studyTimes = studyTimeRepository.findByIsStudying(userId, true)
                .orElseThrow(() -> new StudyTimeNotFoundException("공부 중인 회원이 없습니다."));

        return studyTimes.stream()
                .map(StudyingUserResponse::new)
                .sorted(Comparator.comparing(StudyingUserResponse::getBeginTime))
                .collect(Collectors.toList());
    }


    public StudyTimeResponse endStudy(String apiId) {
        List<StudyTime> studyTimes = studyTimeRepository.findByUserApiIdAndIsStudying(apiId, true);

        if (studyTimes.isEmpty()) {
            throw new StudyTimeNotFoundException("아직 스터디를 시작하지 않았습니다.");
        }

        StudyTime studyTime = studyTimes.get(0);
        studyTime.endStudyTime();

        Double studyScore = StudyTime.getStudyScore(studyTime.getBeginTime(), studyTime.getEndTime());
        dayStatDataService.updateStudyTimeScore(apiId, LocalDate.now(), studyScore);

        return StudyTimeResponse.from(studyTime);
    }

    public StudyTimeResponse updateStudyTime(String apiId, Long studyTimeId, UpdateStudyTimeRequest request) {
        StudyTime curStudyTime = studyTimeRepository.findById(studyTimeId)
                .orElseThrow(() -> new StudyTimeNotFoundException("존재하는 공부 내역이 없습니다."));

        validateUpdateStudyTime(curStudyTime.getBeginTime(), curStudyTime.getEndTime(), request.getBeginTime(), request.getEndTime());

        LocalDate requestedBeginDate = request.getBeginTime().toLocalDate();
        LocalDate requestedEndDate = request.getEndTime().toLocalDate();
        LocalDate currentBeginDate = curStudyTime.getBeginTime().toLocalDate();
        LocalDate currentEndDate = curStudyTime.getEndTime().toLocalDate();

        Double oldStudyScore = curStudyTime.getStudyScore();
        Double newStudyScore = StudyTime.getStudyScore(request.getBeginTime(), request.getEndTime()) - oldStudyScore;

        LocalDateTime curStudyNewBeginTime = request.getBeginTime();
        LocalDateTime curStudyNewEndTime = request.getEndTime();

        if (!requestedBeginDate.isEqual(requestedEndDate)) {
            LocalDateTime newStudyBeginTime = request.getBeginTime();
            LocalDateTime newStudyEndTime = request.getEndTime();

            if (requestedBeginDate.isBefore(currentBeginDate) && requestedEndDate.isEqual(currentEndDate)) {
                curStudyNewBeginTime = LocalDateTime.of(currentBeginDate, LocalTime.MIN);

                newStudyBeginTime = request.getBeginTime();
                newStudyEndTime = LocalDateTime.of(requestedBeginDate, LocalTime.of(23, 59, 59));

                StudyTime newStudyTime = createStudyTime(curStudyTime.getUser(), newStudyBeginTime, newStudyEndTime);
                dayStatDataService.updateStudyTimeScore(apiId, newStudyEndTime.toLocalDate(), newStudyTime.getStudyScore());
            } else if(requestedEndDate.isAfter(currentEndDate)) {
                curStudyNewEndTime = LocalDateTime.of(currentBeginDate, LocalTime.of(23, 59, 59));

                newStudyBeginTime = LocalDateTime.of(requestedEndDate, LocalTime.MIN);
                newStudyEndTime = request.getEndTime();

                createStudyTime(curStudyTime.getUser(), newStudyBeginTime, newStudyEndTime);
                dayStatDataService.updateStudyTimeScore(apiId, newStudyEndTime.toLocalDate(), newStudyScore);

                newStudyScore = StudyTime.getStudyScore(curStudyNewBeginTime, curStudyNewEndTime) - oldStudyScore;
            }
        }

        curStudyTime.updateStudyTime(curStudyNewBeginTime, curStudyNewEndTime);
        dayStatDataService.updateStudyTimeScore(apiId, currentEndDate, newStudyScore);

        return StudyTimeResponse.from(curStudyTime);
    }

    private void validateUpdateStudyTime(LocalDateTime curBeginTime, LocalDateTime curEndTime, LocalDateTime requestedBeginTime, LocalDateTime requestedEndTime) {
        LocalDateTime beginMinDay = curBeginTime.minusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime beginMaxDay = curBeginTime.plusDays(1).withHour(23).withMinute(59).withSecond(59);
        LocalDateTime endMinDay = curEndTime.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endMaxDay = curEndTime.plusDays(1).withHour(23).withMinute(59).withSecond(59);

        if (requestedBeginTime.isBefore(beginMinDay) || requestedBeginTime.isAfter(beginMaxDay)) {
            throw new StudyTimeMeasurementException("요청한 시작시간이 유효범위를 넘었습니다.");
        } else if (requestedEndTime.isBefore(endMinDay) || requestedEndTime.isAfter(endMaxDay)) {
            throw new StudyTimeMeasurementException("요청한 종료시간이 유효범위를 넘었습니다.");
        }
    }


    public void deleteStudyTime(Long studyTimeId) {
        StudyTime studyTime = studyTimeRepository.findById(studyTimeId)
                .orElseThrow(() -> new StudyTimeNotFoundException("존재하는 공부 내역이 없습니다."));

        studyTimeRepository.delete(studyTime);
    }
}
