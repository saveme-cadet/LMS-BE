package com.savelms.api.statistical.dto;

import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.team.TeamEnum;
import com.savelms.core.user.role.RoleEnum;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DayLogDto {

    private String userId;
    private String username;
    private AttendanceStatus checkIn;
    private AttendanceStatus checkOut;
    private RoleEnum role;
    private TeamEnum team;
    private Double attendanceScore;
    private Double absentScore;
    private Double todoSuccessRate;
    private LocalDate tableDay;
    private Double vacation;
    private Double studyTimeScore;
    private Double weekAbsentScore;
    private Double totalScore;

    private DayLogDto(
            String userId,
            String username,
            AttendanceStatus checkIn,
            AttendanceStatus checkOut,
            RoleEnum role,
            TeamEnum team,
            LocalDate tableDay,
            Double vacation,
            Double attendanceScore,
            Double absentScore,
            Double todoSuccessRate,
            Double studyTimeScore,
            Double weekAbsentScore,
            Double totalScore)
    {
        this.userId = userId;
        this.username = username;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.role = role;
        this.team = team;
        this.tableDay = tableDay;
        this.vacation = vacation;
        this.attendanceScore = attendanceScore;
        this.absentScore = absentScore;
        this.todoSuccessRate = todoSuccessRate;
        this.studyTimeScore = studyTimeScore;
        this.weekAbsentScore = weekAbsentScore;
        this.totalScore = totalScore;
    }

    public static DayLogDto of(
            String userId,
            String username,
            AttendanceStatus checkIn,
            AttendanceStatus checkOut,
            RoleEnum role,
            TeamEnum team,
            LocalDate tableDay,
            Double vacation,
            DayStatisticalDataDto dayStatisticalDataDto)
    {
        return new DayLogDto(
                userId,
                username,
                checkIn,
                checkOut,
                role,
                team,
                tableDay,
                vacation,
                dayStatisticalDataDto.getAttendanceScore(),
                dayStatisticalDataDto.getAbsentScore(),
                dayStatisticalDataDto.getTodoSuccessRate(),
                dayStatisticalDataDto.getStudyTimeScore(),
                dayStatisticalDataDto.getWeekAbsentScore(),
                dayStatisticalDataDto.getTotalScore());
    }
}
