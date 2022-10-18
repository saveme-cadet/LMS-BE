package com.savelms.api.report.dto;

import com.savelms.core.monthreport.MonthReport;
import com.savelms.core.statistical.DayStatisticalData;

public class DtoConvert {
    public static MonthReportDto fromMonthReport(MonthReport monthReport, DayStatisticalData dayStatisticalData) {
        return MonthReportDto.builder()
                .grade(monthReport.getGrade())
                .UserName(monthReport.getUserName())
                .UserNickName(monthReport.getUserNickName())
                .score(dayStatisticalData.getAttendanceScore())
                .build();
    }
}
