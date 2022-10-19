package com.savelms.api.report.controller;

import com.savelms.api.report.dto.MonthReportDto;
import com.savelms.api.report.dto.MonthReportResponse;
import com.savelms.api.report.dto.ReportResponse;
import com.savelms.api.report.service.ReportService;
import com.savelms.api.study_time.dto.StudyTimeResponse;
import com.savelms.core.attendance.dto.CheckIOReq;
import com.savelms.core.monthreport.MonthReport;
import com.savelms.core.monthreport.MonthReportRepository;
import com.savelms.core.study_time.domain.entity.StudyTime;
import com.savelms.core.user.domain.repository.UserRepository;
import com.savelms.core.user.role.RoleEnum;
import com.savelms.core.user.role.domain.entity.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final UserRepository userRepository;
    private final MonthReportRepository monthReportRepository;

    //test7
    /*
    위험한 유저 보고
     */
    @PreAuthorize("hasAuthority('week_report.read')")
    @Operation(description = "위험한 유저 보고")
    @GetMapping("/report-user")
    public ResponseEntity<List<ReportResponse>> reportDangerUser(@Parameter(hidden = true) @AuthenticationPrincipal User user) {

            List<ReportResponse> reportResponse = reportService.getDagerUser();

            return ResponseEntity.ok().body(reportResponse);
    }

    @PreAuthorize("hasAuthority('month_report.read')")
    @Operation(description = "출석 등 수 보고")
    @GetMapping("/report-good-user")
    public ResponseEntity<List<MonthReport>> reportGoodUser(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                                               @RequestParam("date") @DateTimeFormat(pattern = StudyTime.DATE_FORMAT) LocalDate date) {
            List<MonthReport> reportResponses = reportService.getGradeUser(date);
            return ResponseEntity.ok().body(reportResponses);
    }

}

