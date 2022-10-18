package com.savelms.api.report.dto;

import com.savelms.core.monthreport.MonthReport;
import com.savelms.core.statistical.DayStatisticalData;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MonthReportResponse {
    String UserName;
    String UserNickName;
    int grade;
}
