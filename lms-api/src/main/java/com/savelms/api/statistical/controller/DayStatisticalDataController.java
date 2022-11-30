package com.savelms.api.statistical.controller;

import com.savelms.api.commondata.APIDataResponse;
import com.savelms.api.statistical.dto.DayLogDto;
import com.savelms.api.statistical.service.DayStatisticalDataService;
import com.savelms.core.study_time.domain.entity.StudyTime;
import com.savelms.core.user.AttendStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DayStatisticalDataController {

    public final DayStatisticalDataService dayStatisticalDataService;


    @GetMapping("/day-logs")
    public APIDataResponse<List<DayLogDto>>
    getDayLogs(
            @RequestParam("date") @DateTimeFormat(pattern = StudyTime.DATE_FORMAT) LocalDate date,
            @RequestParam(value = "attendStatus", required = false) AttendStatus attendStatus)
    {
        if (attendStatus == null || attendStatus == AttendStatus.NOT_PARTICIPATED) {
            return APIDataResponse.badRequest();
        }
        List<DayLogDto> dayLogs = dayStatisticalDataService.getDayLogs(date, attendStatus);

        return APIDataResponse.of(dayLogs);
    }

}
