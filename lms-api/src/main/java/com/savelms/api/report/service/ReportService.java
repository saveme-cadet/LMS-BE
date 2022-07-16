package com.savelms.api.report.service;

import com.savelms.api.report.dto.ReportResponse;
import com.savelms.api.study_time.dto.StudyTimeResponse;
import com.savelms.core.exception.StudyTimeNotFoundException;
import com.savelms.core.report.Report;
import com.savelms.core.report.ReportRepository;
import com.savelms.core.statistical.DayStatisticalDataRepository;
import com.savelms.core.study_time.domain.entity.StudyTime;
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

    private final ReportRepository reportRepository;


    public List<ReportResponse> getDagerUser() {
        List<Report> reportList = reportRepository.findAll();

        return reportList.stream()
                .map(ReportResponse::new)
                .collect(Collectors.toList());
    }
}
