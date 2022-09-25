package com.savelms.api.statistical.controller;

import com.savelms.api.statistical.dto.DayLogDto;
import com.savelms.api.statistical.service.DayStatisticalDataService;
import com.savelms.core.study_time.domain.entity.StudyTime;
import com.savelms.core.user.AttendStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DayStatisticalDataController {

    public final DayStatisticalDataService dayStatisticalDataService;

    @GetMapping("/day-logs")
    public ResponseEntity<List<DayLogDto>> getDayLogs(
            @RequestParam("date") @DateTimeFormat(pattern = StudyTime.DATE_FORMAT) LocalDate date,
            @RequestParam(value = "attendStatus", required = false) AttendStatus attendStatus)
    {
        List<DayLogDto> dayLogs = dayStatisticalDataService.getDayLogs(date, attendStatus);

        return ResponseEntity.ok().body(dayLogs);
    }
}
