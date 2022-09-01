package com.savelms.api.statistical.dto;

import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.team.TeamEnum;
import com.savelms.core.user.role.RoleEnum;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DayLogDto {

    private String userId;
    private Long  attendanceId;
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
            Long  attendanceId,
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
        this.attendanceId = attendanceId;
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
            Long  attendanceId,
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
                attendanceId,
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
                Math.round(dayStatisticalDataDto.getStudyTimeScore() * 100) / 100.0,
                dayStatisticalDataDto.getWeekAbsentScore(),
                Math.round(dayStatisticalDataDto.getTotalScore() * 100) / 100.0);
    }
}
