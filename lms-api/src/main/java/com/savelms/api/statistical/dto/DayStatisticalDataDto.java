package com.savelms.api.statistical.dto;

import com.savelms.core.statistical.DayStatisticalData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DayStatisticalDataDto {

    private Double attendanceScore;
    private Double absentScore;
    private Double todoSuccessRate;
    private Double studyTimeScore;
    private Double weekAbsentScore;
    private Double totalScore;

    static public DayStatisticalDataDto of(
            Double attendanceScore, Double absentScore, Double todoSuccessRate,
            Double studyTimeScore, Double weekAbsentScore, Double totalScore
    ) {
        return new DayStatisticalDataDto(attendanceScore, absentScore, todoSuccessRate, studyTimeScore, weekAbsentScore, totalScore);
    }

    public DayStatisticalDataDto(DayStatisticalData dayStatisticalData) {
        this.attendanceScore = dayStatisticalData.getAttendanceScore();
        this.absentScore = dayStatisticalData.getAbsentScore();
        this.todoSuccessRate = dayStatisticalData.getTodoSuccessRate();
        this.studyTimeScore = dayStatisticalData.getStudyTimeScore();
        this.weekAbsentScore = dayStatisticalData.getWeekAbsentScore();
        this.totalScore = dayStatisticalData.getTotalScore();
    }

}
