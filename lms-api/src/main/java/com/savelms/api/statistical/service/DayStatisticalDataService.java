package com.savelms.api.statistical.service;

import com.savelms.api.attendance.service.AttendanceService;
import com.savelms.api.statistical.dto.DayStatisticalDataDto;
import com.savelms.api.statistical.dto.DayLogDto;
import com.savelms.api.todo.service.TodoService;
import com.savelms.api.user.controller.dto.UserAdminPageDto;
import com.savelms.api.user.service.UserService;
import com.savelms.api.user.userrole.service.UserRoleService;
import com.savelms.api.user.userteam.service.UserTeamService;
import com.savelms.api.vacation.service.VacationService;
import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.attendance.dto.AttendanceDto;
import com.savelms.core.statistical.DayStatisticalData;
import com.savelms.core.statistical.DayStatisticalDataRepository;
import com.savelms.core.team.TeamEnum;
import com.savelms.core.user.AttendStatus;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.role.RoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class DayStatisticalDataService {

    private final TodoService todoService;
    private final VacationService vacationService;
    private final UserRoleService userRoleService;
    private final UserTeamService userTeamService;
    private final AttendanceService attendanceService;
    private final DayStatisticalDataRepository statisticalDataRepository;
    private final UserService userService;

    public List<DayLogDto> getDayLogs(LocalDate date, AttendStatus attendStatus) {
        if (attendStatus == null) {
            return getDayLogsByDate(date);
        }
        return getDayLogsByDateAndAttendStatus(date, attendStatus);
    }

    public List<DayLogDto> getDayLogsAdmin(LocalDate date) {

        return getDayLogsByDate(date);
    }

    private List<DayLogDto> getDayLogsByDate(LocalDate date) {
        List<DayLogDto> list1 = getDayLogsByDateAndAttendStatus(date, AttendStatus.PARTICIPATED);
        List<DayLogDto> list2 = getDayLogsByDateAndAttendStatus(date, AttendStatus.NOT_PARTICIPATED);

        List<DayLogDto> mergedList = new LinkedList<>();
        mergedList.addAll(list1);
        mergedList.addAll(list2);

        return mergedList;
    }

    private List<DayLogDto> getDayLogsByDateAndAttendStatus(LocalDate date, AttendStatus attendStatus) {
        final Map<Long, TeamEnum> teams = userTeamService.findAllUserTeamByDateAndAttendStatus(date, attendStatus);
        final Map<Long, RoleEnum> roles = userRoleService.findAllUserRoleByDateAndAttendStatus(date, attendStatus);
        final Map<Long, AttendanceDto> attendances = attendanceService.getAllAttendanceByDateAndAttendStatus(date, attendStatus);
        final Map<Long, Double> remainingVacations = vacationService.getRemainingVacationByDateAndAttendStatus(date, attendStatus);
        final Map<String, Double> todoProgress = todoService.getTodoProgressAndAttendStatus(date, attendStatus);
        final List<DayStatisticalData> dayStatisticalData = statisticalDataRepository.findAllByDateAndAttendStatus(date, attendStatus);

        return dayStatisticalData.stream()
                .map(statisticalDataToDayLogDtoInAdmin(date, teams, roles, remainingVacations, attendances, todoProgress))
                .collect(Collectors.toUnmodifiableList());
    }

    private Function<DayStatisticalData, DayLogDto> statisticalDataToDayLogDtoInAdmin(
            LocalDate date,
            Map<Long, TeamEnum> teams,
            Map<Long, RoleEnum> roles,
            Map<Long, Double> remainingVacations,
            Map<Long, AttendanceDto> attendances,
            Map<String, Double> progressMap) {
        return (DayStatisticalData statisticalData) -> {
            User user = statisticalData.getUser();
            Long userId = user.getId();
            String apiId = user.getApiId();
            AttendStatus attendStatus = user.getAttendStatus();
            String nickname = user.getNickname();

            Double vacation = remainingVacations.computeIfAbsent(userId, (k) -> 0.0);
            TeamEnum team = teams.computeIfAbsent(userId, (k) -> TeamEnum.NONE);
            RoleEnum role = roles.computeIfAbsent(userId, (k) -> RoleEnum.ROLE_UNAUTHORIZED);
            AttendanceDto attendance = attendances.computeIfAbsent(userId, (k) -> new AttendanceDto(apiId, null, AttendanceStatus.NONE, AttendanceStatus.NONE));
            Double progress = progressMap.computeIfAbsent(apiId, (k) -> 0.0);
            return DayLogDto.of(
                    apiId,
                    attendance.getAttendanceId(),
                    nickname,
                    attendStatus,
                    attendance.getCheckInStatus(),
                    attendance.getCheckOutStatus(),
                    role,
                    team,
                    progress,
                    date,
                    vacation,
                    DayStatisticalDataDto.from(statisticalData));
        };
    }


    public void updateStudyTimeScore(String apiId, LocalDate date, Double score) {
        statisticalDataRepository.findByApiIdAndDate(apiId, date)
                .ifPresent(curStatData -> curStatData.increaseAndDecreaseStudyTimeScore(score));
    }
}
