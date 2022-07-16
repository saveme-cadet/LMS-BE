package com.savelms.api.report.dto;

import com.savelms.core.report.Report;
import lombok.Data;



@Data
public class ReportResponse {
    String UserName;
    String UserNickName;

    public ReportResponse(Report report) {
        this.UserName = report.getUserName();
        this.UserNickName = report.getUserNickName();
    }
}
