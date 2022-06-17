package com.larry.fc.finalproject.api.controller.useradmincontroller;

import com.larry.fc.finalproject.api.dto.AuthUser;
import com.larry.fc.finalproject.api.dto.tododto.RequestTodoDto;
import com.larry.fc.finalproject.api.dto.userstatisticalchartdto.AttendDto;
import com.larry.fc.finalproject.api.dto.userstatisticalchartdto.ObjectDto;
import com.larry.fc.finalproject.api.service.UserStatisticalChartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(originPatterns = "http://15.165.148.236:8080")
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
    public List<ObjectDto> getObject(@Parameter(name="userId", description = "유저 참석 차트 볼 id", in = ParameterIn.QUERY)  @RequestParam(required = false) AuthUser authUser,
                                     @Parameter(name = "date", description = "date=2022-02-11", in = ParameterIn.QUERY) @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return userStatisticalChartService.getUserObjectStatic(authUser.getId(), date);
    }
}
