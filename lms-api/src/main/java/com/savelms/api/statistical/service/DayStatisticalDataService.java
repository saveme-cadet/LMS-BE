package com.savelms.api.statistical.service;

import com.savelms.api.statistical.dto.DayStatisticalDataDto;
import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.statistical.DayStatisticalData;
import com.savelms.core.statistical.DayStatisticalDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class DayStatisticalDataService {

    private final CalendarRepository calendarRepository;
    private final DayStatisticalDataRepository statisticalDataRepository;

//    public DayStatisticalDataDto getDayStatisticalData(String userId, LocalDate date) {
//        Calendar calendar = calendarRepository.findByDate(date)
//            .orElseThrow(() -> new EntityNotFoundException("해당 날짜의 캘린더가 존재하지 않습니다."));
//
//        DayStatisticalData dayStatisticalData = statisticalDataRepository.findByUserIdAndDate(userId, date)
//                .orElseThrow(EntityNotFoundException::new);
//
//        return DayStatisticalDataDto.builder()
//                .userId(dayStatisticalData.getUserId())
//                .date(dayStatisticalData.getDate())
//                .attendanceStatus(dayStatisticalData.getAttendanceStatus())
//                .build();

//    }

    /**
     * 조회
     * */
    public DayStatisticalDataDto getDayStatisticalData(String username, LocalDate date) {
        DayStatisticalData dayStatisticalData = statisticalDataRepository.findByUsernameAndDate(username, date)
                .orElseThrow(() -> new EntityNotFoundException("존재하는 통계 테이블이 없습니다."));

        return new DayStatisticalDataDto(dayStatisticalData);
    }


    /**
     * 수정
     * */
    public void updateAttendanceAndAbsentScore(String username, AttendanceStatus status, LocalDate date) {
        statisticalDataRepository.findByUsernameAndDate(username, date)
                .ifPresentOrElse(dayStatisticalData -> {
                    dayStatisticalData.updateAttendanceScore(status.getAttendanceScore());
                    dayStatisticalData.updateAbsentScore(status.getAttendancePenalty());

                    if (status.equals(AttendanceStatus.ABSENT)) {
                        dayStatisticalData.updateWeekAbsentScore(1.0D);
                    }

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

    public void updateStudyTimeScore(String username, Double studyScore, LocalDate date) {
        statisticalDataRepository.findByUsernameAndDate(username, date)
                .ifPresentOrElse(dayStatisticalData -> {
                    dayStatisticalData.updateStudyTimeScore(studyScore);
                }, () -> {
                    log.warn("통계 테이블에 당일 레코드가 존재하지 않아 점수가 업데이트되지 않았습니다.");
                });
    }

    public void updateTotalScore(String username, LocalDate date) {
        DayStatisticalData dayStatisticalData = statisticalDataRepository.findByUsernameAndDate(username, date)
                .orElseThrow(() -> new EntityNotFoundException("존재하는 통계 테이블이 없습니다."));

        String month = String.format("%02d", date.getMonth().getValue());
        statisticalDataRepository.findTotalStudyTimePerMonth(username, month)
                .ifPresent(dayStatisticalData::updateTotalScore);
    }

}
