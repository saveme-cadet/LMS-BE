package com.savelms.api.report.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MonthReportDto {
    String UserName;
    String UserNickName;
    int grade;
    double score;
}
