package com.savelms.api.report.service;

import com.savelms.api.report.dto.ReportResponse;
import com.savelms.core.weekreport.WeekReport;
import com.savelms.core.weekreport.WeekReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {

    private final WeekReportRepository weekReportRepository;


    public List<ReportResponse> getDagerUser() {
        List<WeekReport> reportList = weekReportRepository.findAll();

        return reportList.stream()
                .map(ReportResponse::new)
                .collect(Collectors.toList());
    }
}
