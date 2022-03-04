package com.larry.fc.finalproject.api.controller.useradmincontroller;

import com.larry.fc.finalproject.api.dto.AuthUser;
import com.larry.fc.finalproject.api.dto.userstatisticalchartdto.AttendDto;
import com.larry.fc.finalproject.api.dto.userstatisticalchartdto.ObjectDto;
import com.larry.fc.finalproject.api.service.UserStatisticalChartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "유저 차트 보여주기")
@RequiredArgsConstructor
@RequestMapping("/api/userchart")
@RestController
public class UserStatisticalChartController {
    private final UserStatisticalChartService userStatisticalChartService;

    @Operation(description = "유저 참석 차트 보여주기")
    @GetMapping("/attendstatic")
    public List<AttendDto> getAttend(@Parameter(name="userId", description = "유저 참석 차트 볼 id") AuthUser authUser){
        return userStatisticalChartService.getUserAttendStatic(authUser.getId());
    }

    @Operation(description = "유저 목표 차트 보여주기")
    @GetMapping("/objectstatic")
    public List<ObjectDto> getObject(@Parameter(name="userId", description = "유저 목표 차트 볼 id") AuthUser authUser){
        return userStatisticalChartService.getUserObjectStatic(authUser.getId());
    }
}
