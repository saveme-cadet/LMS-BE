package com.savelms.api.study_time.service;

import com.savelms.api.statistical.service.DayStatisticalDataService;
import com.savelms.api.study_time.dto.StudyTimeResponse;
import com.savelms.api.study_time.dto.StudyingUserResponse;
import com.savelms.api.study_time.dto.UpdateStudyTimeRequest;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.exception.StudyTimeNotFoundException;
import com.savelms.core.statistical.DayStatisticalDataRepository;
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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyTimeService {

    private final UserRepository userRepository;
    private final StudyTimeRepository studyTimeRepository;
    private final CalendarRepository calendarRepository;
    private final DayStatisticalDataService statisticalDataService;


    /**
     * 생성
     * */
    @Transactional
    public StudyTimeResponse startStudy(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Calendar calendar = calendarRepository.findAllByDate(LocalDate.now());

        StudyTime studyTime = StudyTime.of(user, calendar);
        studyTimeRepository.save(studyTime);
        
        return StudyTimeResponse.from(studyTime);
    }


    /**
     * 조회
     * */
    public List<StudyTimeResponse> getStudyTimes(String username) {
        List<StudyTime> studyTimes = studyTimeRepository.findByUsername(username)
                .orElseThrow(() -> new StudyTimeNotFoundException("존재하는 공부 내역이 없습니다."));


        return studyTimes.stream()
                .map(StudyTimeResponse::from)
                .collect(Collectors.toList());
    }

    public List<StudyTimeResponse> getTodayStudyTimes(String username) {
        List<StudyTime> studyTimes = studyTimeRepository.findByUsernameAndDate(username, LocalDate.now())
                .orElseThrow(() -> new StudyTimeNotFoundException("존재하는 공부 내역이 없습니다."));

        return studyTimes.stream()
                .map(StudyTimeResponse::from)
                .collect(Collectors.toList());
    }

    public List<StudyTimeResponse> getStudyTimesByDate(String username, LocalDate createdDate) {
        List<StudyTime> studyTimes = studyTimeRepository.findByUsernameAndDate(username, createdDate)
                .orElseThrow(() -> new StudyTimeNotFoundException("존재하는 공부 내역이 없습니다."));

        return studyTimes.stream()
                .map(StudyTimeResponse::from)
                .collect(Collectors.toList());
    }

    public List<StudyingUserResponse> getStudyingUser() {
        List<StudyTime> studyTimes = studyTimeRepository.findByIsStudying(true)
                .orElseThrow(() -> new StudyTimeNotFoundException("공부 중인 회원이 없습니다."));

        return studyTimes.stream()
                .map(StudyingUserResponse::new)
                .collect(Collectors.toList());
    }


    /**
     * 수정
     * */
    @Transactional
    public StudyTimeResponse endStudy(String username) {
        StudyTime studyTime = studyTimeRepository.findByUsernameAndIsStudying(username, true)
                .orElseThrow(() -> {throw new StudyTimeNotFoundException("공부 중이 아닙니다.");});

        studyTime.endStudyTime();

        statisticalDataService.updateStudyTimeScore(username,
                StudyTime.getStudyScore(studyTime.getBeginTime(), studyTime.getEndTime()), LocalDate.now());

        return StudyTimeResponse.from(studyTime);
    }

    @Transactional
    public StudyTimeResponse updateStudyTime(Long studyTimeId, UpdateStudyTimeRequest request) {
        StudyTime studyTime = studyTimeRepository.findById(studyTimeId)
                .orElseThrow(() -> new StudyTimeNotFoundException("존재하는 공부 내역이 없습니다."));

        String date = studyTime.getCreatedAt().format(DateTimeFormatter.ofPattern(StudyTime.DATE_FORMAT));

        LocalDateTime beginTime = stringToLocalDateTime(date + " " + request.getBeginTime());
        LocalDateTime endTime = stringToLocalDateTime(date + " " + request.getEndTime());

        studyTime.updateStudyTime(beginTime, endTime);

        return StudyTimeResponse.from(studyTime);
    }

    private LocalDateTime stringToLocalDateTime(String dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return LocalDateTime.parse(dateTime, dateTimeFormatter);
    }


    /**
     * 삭제
     * */
    @Transactional
    public void deleteStudyTime(Long studyTimeId) {
        StudyTime studyTime = studyTimeRepository.findById(studyTimeId)
                .orElseThrow(() -> new StudyTimeNotFoundException("존재하는 공부 내역이 없습니다."));

        studyTimeRepository.delete(studyTime);
    }
}
