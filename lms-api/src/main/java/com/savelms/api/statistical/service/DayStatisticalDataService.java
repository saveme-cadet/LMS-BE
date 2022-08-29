package com.savelms.api.statistical.service;

import com.savelms.api.attendance.service.AttendanceService;
import com.savelms.api.statistical.dto.DayStatisticalDataDto;
import com.savelms.api.statistical.dto.DayLogDto;
import com.savelms.api.user.userrole.service.UserRoleService;
import com.savelms.api.user.userteam.service.UserTeamService;
import com.savelms.api.vacation.service.VacationService;
import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.attendance.dto.AttendanceDto;
import com.savelms.core.statistical.DayStatisticalData;
import com.savelms.core.statistical.DayStatisticalDataRepository;
import com.savelms.core.team.TeamEnum;
import com.savelms.core.user.role.RoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class DayStatisticalDataService {

    private final VacationService vacationService;
    private final UserRoleService userRoleService;
    private final UserTeamService userTeamService;
    private final AttendanceService attendanceService;
    private final DayStatisticalDataRepository statisticalDataRepository;

    public List<DayLogDto> getDayLogs() {
        final Map<Long, TeamEnum> teams = userTeamService.findAllUserTeamByDate(LocalDate.now());

        return null;
    }

    public List<DayLogDto> getDayLogs(LocalDate date) {
        final Map<Long, TeamEnum> teams = userTeamService.findAllUserTeamByDate(date); //1
        final Map<Long, RoleEnum> roles = userRoleService.findAllUserRoleByDate(date); // 1
        final Map<Long, AttendanceDto> attendances = attendanceService.getAllAttendanceByDate(date); // 5
        final Map<Long, Double> remainingVacations = vacationService.getAllRemainingVacationByDate(date); // 1
        final List<DayStatisticalData> dayStatisticalData = statisticalDataRepository.findAllByDate(date); // 5

        return dayStatisticalData.stream()
                .map((statisticalData) -> {
                    Long userId = statisticalData.getUser().getId();
                    String apiId = statisticalData.getUser().getApiId();
                    String nickname = statisticalData.getUser().getNickname();

                    Double vacation = remainingVacations.computeIfAbsent(userId, (k) -> 0.0);
                    TeamEnum team = teams.computeIfAbsent(userId, (k) -> TeamEnum.NONE);
                    RoleEnum role = roles.computeIfAbsent(userId, (k) -> RoleEnum.ROLE_UNAUTHORIZED);
                    AttendanceDto attendance = attendances.computeIfAbsent(userId,
                            (k) -> new AttendanceDto(apiId, 0L, AttendanceStatus.NONE, AttendanceStatus.NONE));

                    return DayLogDto.of(apiId, attendance.getAttendanceId(), nickname,
                            attendance.getCheckInStatus(), attendance.getCheckOutStatus(),
                            role, team, date, vacation, DayStatisticalDataDto.from(statisticalData));
                })
                .collect(Collectors.toUnmodifiableList());
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
