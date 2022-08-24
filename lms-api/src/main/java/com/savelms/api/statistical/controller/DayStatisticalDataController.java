package com.savelms.api.statistical.controller;

import com.savelms.api.statistical.dto.DayStatisticalDataGetResponse;
import com.savelms.api.statistical.service.DayStatisticalDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DayStatisticalDataController {

    public DayStatisticalDataService dayStatisticalDataService;

//    @GetMapping("/users/{userId}/day-statistical-data")
//    public DayStatisticalDataGetResponse getDayStatisticalData(@PathVariable("userId") String userId,
//        @RequestParam("date") String date) {
//
//    }

}
