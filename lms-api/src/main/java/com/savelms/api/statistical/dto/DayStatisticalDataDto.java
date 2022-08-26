package com.savelms.api.statistical.dto;

import com.savelms.core.statistical.DayStatisticalData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DayStatisticalDataDto {

    private Double attendanceScore;
    private Double absentScore;
    private Double todoSuccessRate;
    private Double studyTimeScore;
    private Double weekAbsentScore;
    private Double totalScore;

    private DayStatisticalDataDto(
            Double attendanceScore, Double absentScore, Double todoSuccessRate,
            Double studyTimeScore, Double weekAbsentScore, Double totalScore)
    {
        this.attendanceScore = attendanceScore;
        this.absentScore = absentScore;
        this.todoSuccessRate = todoSuccessRate;
        this.studyTimeScore = studyTimeScore;
        this.weekAbsentScore = weekAbsentScore;
        this.totalScore = totalScore;
    }

    static public DayStatisticalDataDto from(DayStatisticalData dayStatisticalData) {
        return new DayStatisticalDataDto(
                dayStatisticalData.getAttendanceScore(),
                dayStatisticalData.getAbsentScore(),
                dayStatisticalData.getTodoSuccessRate(),
                dayStatisticalData.getStudyTimeScore(),
                dayStatisticalData.getWeekAbsentScore(),
                dayStatisticalData.getTotalScore());
    }
}
