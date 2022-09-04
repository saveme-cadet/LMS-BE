package com.savelms.api.study_time.service;

import com.savelms.api.statistical.service.DayStatisticalDataService;
import com.savelms.api.study_time.dto.StudyTimeResponse;
import com.savelms.api.study_time.dto.StudyingUserResponse;
import com.savelms.api.study_time.dto.UpdateStudyTimeRequest;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.exception.StudyTimeNotFoundException;
import com.savelms.core.statistical.DayStatisticalData;
import com.savelms.core.statistical.DayStatisticalDataRepository;
import com.savelms.core.study_time.domain.entity.StudyTime;
import com.savelms.core.study_time.domain.repository.StudyTimeRepository;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private final DayStatisticalDataRepository statisticalDataRepository;


    /**
     * 생성
     * */
    @Transactional
    public StudyTimeResponse startStudy(String apiId) {
        User user = userRepository.findByApiId(apiId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Calendar calendar = calendarRepository.findAllByDate(LocalDate.now());

        StudyTime studyTime = StudyTime.of(user, calendar);
        studyTimeRepository.save(studyTime);
        
        return StudyTimeResponse.from(studyTime);
    }


    /**
     * 조회
     * */
    public List<StudyTimeResponse> getStudyTimes(String apiId) {
        List<StudyTime> studyTimes = studyTimeRepository.findByUserApiId(apiId)
                .orElseThrow(() -> new StudyTimeNotFoundException("존재하는 공부 내역이 없습니다."));


        return studyTimes.stream()
                .map(StudyTimeResponse::from)
                .collect(Collectors.toList());
    }

    public List<StudyTimeResponse> getTodayStudyTimes(String apiId) {
        List<StudyTime> studyTimes = studyTimeRepository.findByUserApiIdAndDate(apiId, LocalDate.now())
                .orElseThrow(() -> new StudyTimeNotFoundException("존재하는 공부 내역이 없습니다."));

        return studyTimes.stream()
                .map(StudyTimeResponse::from)
                .collect(Collectors.toList());
    }

    public List<StudyTimeResponse> getStudyTimesByDate(String apiId, LocalDate createdDate) {
        List<StudyTime> studyTimes = studyTimeRepository.findByUserApiIdAndDate(apiId, createdDate)
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
    public StudyTimeResponse endStudy(String apiId) {
        List<StudyTime> studyTimes = studyTimeRepository.findByUserApiIdAndIsStudying(apiId, true);
        if (studyTimes.isEmpty()) {
            throw new StudyTimeNotFoundException("아직 스터디를 시작하지 않았습니다.");
        }

        StudyTime studyTime = studyTimes.get(0);
        studyTime.endStudyTime();

        Double studyScore = StudyTime.getStudyScore(studyTime.getBeginTime(), studyTime.getEndTime());
        statisticalDataService.updateStudyTimeScore(apiId, studyScore, LocalDate.now());
        return StudyTimeResponse.from(studyTime);
    }

    @Transactional
    public StudyTimeResponse updateStudyTime(String apiId, Long studyTimeId, UpdateStudyTimeRequest request) {
        StudyTime studyTime = studyTimeRepository.findById(studyTimeId)
                .orElseThrow(() -> new StudyTimeNotFoundException("존재하는 공부 내역이 없습니다."));
        DayStatisticalData stats = statisticalDataRepository.findByApiIdAndDate(apiId, studyTime.getCreatedAt().toLocalDate())
                .orElseThrow(() -> new EntityNotFoundException("존재하는 통계 내역이 없습니다."));

        LocalDateTime beginTime = LocalDateTime.of(studyTime.getCreatedAt().toLocalDate(), LocalTime.parse(request.getBeginTime()));
        LocalDateTime endTime = LocalDateTime.of(studyTime.getCreatedAt().toLocalDate(), LocalTime.parse(request.getEndTime()));

        Double oldStudyScore = studyTime.getStudyScore();
        studyTime.updateStudyTime(beginTime, endTime);
        Double newStudyScore = StudyTime.getStudyScore(studyTime.getBeginTime(), studyTime.getEndTime());
        double differenceScore = newStudyScore - oldStudyScore;

        statisticalDataRepository.updateStudyTimeScore(apiId, differenceScore, LocalDateTime.of(studyTime.getCreatedAt().toLocalDate(), LocalTime.MIN));
        return StudyTimeResponse.from(studyTime);
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
