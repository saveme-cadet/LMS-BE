package com.savelms.api.attendance.controller;

import com.savelms.api.attendance.service.AttendanceService;
import com.savelms.api.commondata.APIDataResponse;
import com.savelms.api.todo.controller.dto.ListResponse;
import com.savelms.api.user.service.UserService;
import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.attendance.domain.entity.Attendance;
import com.savelms.core.attendance.domain.repository.AttendanceRepository;
import com.savelms.core.attendance.dto.AttendanceDto;
import com.savelms.core.attendance.dto.CheckIOReq;
import com.savelms.core.exception.NoPermissionException;
import com.savelms.core.user.domain.repository.UserRepository;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@Slf4j
@Builder
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;

    @PreAuthorize("hasAuthority('attendance.update') OR "
        + "hasAuthority('user.attendance.update')")
    @PatchMapping("/users/{userId}/{attendanceId}/checkin")
    public APIDataResponse<ResponseEntity<String>> userCheckIn(
            @Parameter(hidden = true) @AuthenticationPrincipal User sessionUser,
            @PathVariable("userId") String userAPiId,
            @PathVariable("attendanceId") Long attendanceId,
            @Valid @RequestBody CheckIOReq reqBody) {
        /**
         * 로그인 했는지 확인하기 -> 시큐리티
         * 바디 파라미터 형식 확인하기 -> 컨트롤러 @Valid
         * 권한 확인하기 -> 서비스 Exceptions
         * 출결표 있는지 확인하기 -> 서비스
         */

        attendanceService.checkIn(attendanceId, userAPiId, reqBody.getStatus());

        return APIDataResponse.of(new ResponseEntity<>("OK", HttpStatus.OK));

    }



    @PreAuthorize("hasAuthority('attendance.update') OR "
        + "hasAuthority('user.attendance.update')")
    @PatchMapping("/users/{userId}/{attendanceId}/checkout")
    public APIDataResponse<ResponseEntity<String>> userCheckOut(
            @Parameter(hidden = true) @AuthenticationPrincipal User sessionUser,
            @PathVariable("userId") String userApiId,
            @PathVariable("attendanceId") Long attendanceId,
            @Valid @RequestBody CheckIOReq reqBody) {
        /**
         * 로그인 했는지 확인하기 -> 시큐리티
         * 바디 파라미터 형식 확인하기 -> 컨트롤러 @Valid
         * 권한 확인하기 -> 서비스 Exceptions
         * 출결표 있는지 확인하기 -> 서비스
         */
        attendanceService.checkOut(attendanceId, userApiId, reqBody.getStatus());

        return APIDataResponse.of(new ResponseEntity<>("OK", HttpStatus.OK));
    }


    @PreAuthorize("hasAuthority('attendance.read')")
    @GetMapping("")
    public APIDataResponse<ListResponse<AttendanceDto>> getAttendanceListByDate(
            @Parameter(name = "date", description = "date=2022-02-11", in = ParameterIn.QUERY)
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<AttendanceDto> attendanceDtoList = attendanceService.getAllAttendanceByDate(
                        date == null ? LocalDate.now() : date)
                .values()
                .stream()
                .collect(Collectors.toList());

        return APIDataResponse.of(ListResponse.<AttendanceDto>builder()
                .content(attendanceDtoList)
                .count(attendanceDtoList.size())
                .build());
    }

}