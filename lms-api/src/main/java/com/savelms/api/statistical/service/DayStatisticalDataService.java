package com.savelms.api.statistical.service;

import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.statistical.DayStatisticalDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class DayStatisticalDataService {

    private final DayStatisticalDataRepository statisticalDataRepository;

    public void updateAttendanceAndAbsentScore(String username, AttendanceStatus status) {
        statisticalDataRepository.findByUsernameAndDate(username, LocalDate.now())
                .ifPresentOrElse(dayStatisticalData -> {
                    dayStatisticalData.updateAttendanceScore(status.getAttendanceScore());
                    dayStatisticalData.updateAbsentScore(status.getAttendancePenalty());
                }, () -> {
                    log.warn("통계 테이블에 당일 레코드가 존재하지 않아 점수가 업데이트되지 않았습니다.");
                });
    }

    public void updateTodoSuccessRate(String apiId, Double progress, LocalDate date) {
        statisticalDataRepository.findByApiIdAndDate(apiId, date)
                .ifPresentOrElse(dayStatisticalData -> {
                    dayStatisticalData.updateTodoSuccessRate(progress);
                }, () -> {
                    log.warn("통계 테이블에 당일 레코드가 존재하지 않아 점수가 업데이트되지 않았습니다.");
                });
    }

    public void updateStudyTimeScore(String username, Double studyScore) {
        statisticalDataRepository.findByUsernameAndDate(username, LocalDate.now())
                .ifPresentOrElse(dayStatisticalData -> {
                    dayStatisticalData.updateStudyTimeScore(studyScore);
                }, () -> {
                    log.warn("통계 테이블에 당일 레코드가 존재하지 않아 점수가 업데이트되지 않았습니다.");
                });
    }

}
