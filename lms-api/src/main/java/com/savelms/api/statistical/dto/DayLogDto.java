package com.savelms.api.statistical.dto;

import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.team.TeamEnum;
import com.savelms.core.user.AttendStatus;
import com.savelms.core.user.role.RoleEnum;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DayLogDto {

    private LocalDate tableDay;
    private String userId;
    private Long attendanceId;
    private String username;
    private RoleEnum role;
    private TeamEnum team;
    private Double vacation;
    private AttendStatus attendStatus;
    private AttendanceStatus checkIn;
    private AttendanceStatus checkOut;
    private Double todoSuccessRate;
    private Double weekAbsentScore;
    private Double attendanceScore;
    private Double totalAbsentScore;

    protected DayLogDto() {}

    private DayLogDto(
            String userId,
            Long  attendanceId,
            String username,
            AttendStatus attendStatus,
            AttendanceStatus checkIn,
            AttendanceStatus checkOut,
            RoleEnum role,
            TeamEnum team,
            LocalDate tableDay,
            Double vacation,
            Double attendanceScore,
            Double todoSuccessRate,
            Double weekAbsentScore,
            Double totalScore)
    {
        this.userId = userId;
        this.attendanceId = attendanceId;
        this.username = username;
        this.attendStatus = attendStatus;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.role = role;
        this.team = team;
        this.tableDay = tableDay;
        this.vacation = vacation;
        this.attendanceScore = attendanceScore;
        this.todoSuccessRate = todoSuccessRate;
        this.weekAbsentScore = weekAbsentScore;
        this.totalAbsentScore = totalScore;
    }

    public static DayLogDto of(
            String userId,
            Long  attendanceId,
            String username,
            AttendStatus attendStatus,
            AttendanceStatus checkIn,
            AttendanceStatus checkOut,
            RoleEnum role,
            TeamEnum team,
            Double progress,
            LocalDate tableDay,
            Double vacation,
            DayStatisticalDataDto dayStatisticalDataDto)
    {
        return new DayLogDto(
                userId,
                attendanceId,
                username,
                attendStatus,
                checkIn,
                checkOut,
                role,
                team,
                tableDay,
                vacation,
                dayStatisticalDataDto.getAttendanceScore(),
                progress,
                dayStatisticalDataDto.getWeekAbsentScore(),
                Math.round(dayStatisticalDataDto.getTotalScore() * 100) / 100.0);
    }
}
