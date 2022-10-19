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
import com.savelms.core.attendance.domain.entity.Attendance;
import com.savelms.core.attendance.dto.AttendanceDto;
import com.savelms.core.statistical.DayStatisticalData;
import com.savelms.core.statistical.DayStatisticalDataRepository;
import com.savelms.core.team.TeamEnum;
import com.savelms.core.user.AttendStatus;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
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
    private final UserRepository userRepository;

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
        List<User> users = userRepository.findAllByAttendStatus(attendStatus);

        final Map<Long, TeamEnum> teams = userTeamService.findAllUserTeamByDateAndAttendStatus(date, attendStatus);
        final Map<Long, RoleEnum> roles = userRoleService.findAllUserRoleByDateAndAttendStatus(date, attendStatus);
        final Map<Long, AttendanceDto> attendances = attendanceService.getAllAttendanceByDateAndAttendStatus(date, attendStatus);
        final Map<Long, Double> remainingVacations = vacationService.getRemainingVacationByDateAndAttendStatus(date, attendStatus);
        final Map<String, Double> todoProgress = todoService.getTodoProgressAndAttendStatus(date, attendStatus);
        final List<DayStatisticalData> dayStatisticalData = statisticalDataRepository.findAllByDateAndAttendStatus(date, attendStatus);
        Map<Long, DayStatisticalDataDto> dayStatisticalDataDtoMap = dayStatisticalData.stream()
            .collect(
                Collectors.toMap((d) -> d.getUser().getId(), d -> DayStatisticalDataDto.from(d)));
        AttendanceDto defaultAttendance = new AttendanceDto();
        defaultAttendance.setAttendanceId(null);
        defaultAttendance.setCheckInStatus(AttendanceStatus.NONE);
        defaultAttendance.setCheckOutStatus(AttendanceStatus.NONE);
        return users.stream()
            .map(u -> (
                DayLogDto.of(u.getApiId(),
                    attendances.getOrDefault(u.getId(), defaultAttendance).getAttendanceId(),
                    u.getNickname(),
                    u.getAttendStatus(),
                    attendances.getOrDefault(u.getId(), defaultAttendance).getCheckInStatus(),
                    attendances.getOrDefault(u.getId(), defaultAttendance).getCheckOutStatus(),
                    roles.get(u.getId()),
                    teams.get(u.getId()),
                    todoProgress.getOrDefault(u.getApiId(), 0.0),
                    date,
                    remainingVacations.getOrDefault(u.getId(), 0.0),
                    dayStatisticalDataDtoMap.getOrDefault(u.getId(),
                        DayStatisticalDataDto.builder()
                            .attendanceScore(0.0)
                            .absentScore(0.0)
                            .todoSuccessRate(0.0)
                            .studyTimeScore(0.0)
                            .totalScore(0.0)
                            .weekAbsentScore(0.0)
                            .build()
                    ))
            ))
            .collect(Collectors.toList());

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

            Double vacation = remainingVacations.getOrDefault(userId, 0.0);
            TeamEnum team = teams.getOrDefault(userId,TeamEnum.NONE);
            RoleEnum role = roles.getOrDefault(userId,RoleEnum.ROLE_UNAUTHORIZED);
            AttendanceDto attendance = attendances.getOrDefault(userId,new AttendanceDto(apiId, null, AttendanceStatus.NONE, AttendanceStatus.NONE));
            Double progress = progressMap.getOrDefault(apiId, 0.0);
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
                .ifPresentOrElse(curStatData -> curStatData.increaseAndDecreaseStudyTimeScore(score),
                        () -> log.warn("[study_time] - 통계 데이터가 존재하지 않습니다."));
    }
}
