package com.savelms.api.statistical.service;

import com.savelms.api.attendance.service.AttendanceService;
import com.savelms.api.statistical.dto.DayStatisticalDataDto;
import com.savelms.api.statistical.dto.DayLogDto;
import com.savelms.api.vacation.service.VacationService;
import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.attendance.dto.AttendanceDto;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.statistical.DayStatisticalData;
import com.savelms.core.statistical.DayStatisticalDataRepository;
import com.savelms.core.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.savelms.core.attendance.domain.AttendanceStatus.NONE;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class DayStatisticalDataService {

    private final CalendarRepository calendarRepository;
    private final AttendanceService attendanceService;
    private final VacationService vacationService;
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

    public List<DayLogDto> getDayLogs(LocalDate date) {
        final List<DayLogDto> userLogDtoList = new ArrayList<>();
        final Map<Long, AttendanceDto> attendances = attendanceService.getAllAttendanceByDate(date);
        final Map<Long, Double> remainingVacations = vacationService.getAllRemainingVacationByDate(date);
        final List<DayStatisticalData> dayStatisticalData = statisticalDataRepository.findAllByDate(date);

        for (DayStatisticalData statisticalData : dayStatisticalData) {
            Long userId = statisticalData.getUser().getId();
            String apiId = statisticalData.getUser().getApiId();
            String nickname = statisticalData.getUser().getNickname();
            Double vacation = remainingVacations.computeIfAbsent(userId, (k) -> 0.0);
            AttendanceDto attendance = attendances.computeIfAbsent(userId, (k) -> new AttendanceDto(apiId, NONE, NONE));

            DayLogDto userLogDto = DayLogDto.of(
                    apiId, nickname, attendance.getCheckInStatus(), attendance.getCheckOutStatus(),
                    null, null, date, vacation, DayStatisticalDataDto.from(statisticalData));
            userLogDtoList.add(userLogDto);
        }
        return userLogDtoList;
    }

    public DayStatisticalDataDto getDayStatisticalData(String username, LocalDate date) {
        DayStatisticalData dayStatisticalData = statisticalDataRepository.findByUsernameAndDate(username, date)
                .orElseThrow(() -> new EntityNotFoundException("존재하는 통계 테이블이 없습니다."));

        return DayStatisticalDataDto.from(dayStatisticalData);
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
