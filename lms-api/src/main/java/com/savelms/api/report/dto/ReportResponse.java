package com.savelms.api.report.dto;

import com.savelms.core.weekreport.WeekReport;
import lombok.Data;



@Data
public class ReportResponse {
    String UserName;
    String UserNickName;



    public ReportResponse(WeekReport report){
        this.UserName = report.getUserName();
        this.UserNickName = report.getUserNickName();
    }
}
