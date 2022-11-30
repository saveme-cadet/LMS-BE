package com.savelms.api.report.service;

import com.savelms.api.report.dto.DtoConvert;
import com.savelms.api.report.dto.MonthReportDto;
import com.savelms.api.report.dto.MonthReportResponse;
import com.savelms.api.report.dto.ReportResponse;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.monthreport.MonthReport;
import com.savelms.core.monthreport.MonthReportRepository;
import com.savelms.core.statistical.DayStatisticalData;
import com.savelms.core.statistical.DayStatisticalDataRepository;
import com.savelms.core.user.domain.repository.UserRepository;
import com.savelms.core.weekreport.WeekReport;
import com.savelms.core.weekreport.WeekReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {

    private final WeekReportRepository weekReportRepository;
    private final MonthReportRepository monthReportRepository;
    private final DayStatisticalDataRepository dayStatisticalDataRepository;
    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;

    public List<ReportResponse> getDagerUser() {
        List<WeekReport> reportList = weekReportRepository.findAll();

        return reportList.stream()
                .map(ReportResponse::new)
                .collect(Collectors.toList());
    }


    public List<MonthReport> getGradeUser(LocalDate date) {
        List<MonthReport> reportStream = monthReportRepository.findAll()
                .stream().filter(x -> x.getMonth().getMonth().equals(date.getMonth()))
                .collect(Collectors.toList());
//        MonthReportDto[] dtos = new MonthReportDto[reportStream.size()];
//        for (int i = 0; i < reportStream.size(); i++) {
//            LocalDate lastDay = date.withDayOfMonth(date.lengthOfMonth());
//            Optional<DayStatisticalData> dayStatisticalData = dayStatisticalDataRepository.findAllByUser_idAndCalendar_id(userRepository.findByUsername(reportStream.get(i).getUserName()).get().getId(),
//                    calendarRepository.findByDate(lastDay).get().getId());
//            dtos[i] = DtoConvert.fromMonthReport(reportStream.get(i), dayStatisticalData.get());
//        }
//        List<MonthReportDto> MonthDtoList = Arrays.asList(dtos);
        return reportStream;
    }
}
