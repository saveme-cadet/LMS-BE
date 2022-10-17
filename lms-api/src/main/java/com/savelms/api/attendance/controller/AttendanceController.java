package com.savelms.api.attendance.controller;

import com.savelms.api.attendance.service.AttendanceService;
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
    public ResponseEntity<String> userCheckIn(
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

        //try {

//            if (userRepository.findById(userAPiId).get().getUserRoles().)
            //유저 찾은 후, 정상적으로 유저를 찾으면 비즈니스 로직 실행
            attendanceService.checkIn(attendanceId, userAPiId, reqBody.getStatus());


//        final Optional<Attendance> original = attendanceRepository.findAttendanceById(attendanceId);
//                original.ifPresent(user -> {
//                    attendanceService.checkIn(attendanceId, userAPiId, reqBody.getStatus());
//                });

//        userRepository.findFirstByApiId(userAPiId)
//                .ifPresent(user -> attendanceService.checkIn(attendanceId, user, reqBody.getStatus()));
        //정상적으로 로직이 실행 된 경우
        return new ResponseEntity<>("OK", HttpStatus.OK);
//        } catch (NoPermissionException e) {
//            //변경 권한이 없는 유저가 변경을 시도한 경우
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        } catch (NoSuchElementException e) {
//            //잘못된 출결표Id로 요청한 경우
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        }
    }



    @PreAuthorize("hasAuthority('attendance.update') OR "
        + "hasAuthority('user.attendance.update')")
    @PatchMapping("/users/{userId}/{attendanceId}/checkout")
    public ResponseEntity<String> userCheckOut(
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


        //    try {
        //유저 찾은 후, 정상적으로 유저를 찾으면 비즈니스 로직 실행
//        userRepository.findFirstByApiId(userApiId)
//                .ifPresent(user -> attendanceService.checkOut(attendanceId, user, reqBody.getStatus()));
        //정상적으로 로직이 실행 된 경우
        return new ResponseEntity<>("OK", HttpStatus.OK);
//        } catch (NoPermissionException e) {
//            //변경 권한이 없는 유저가 변경을 시도한 경우
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        } catch (IllegalStateException e) {
//            //체크인을 하지 않고 체크아웃을 시도한 경우
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        } catch (NoSuchElementException e) {
//            //잘못된 출결표Id로 요청한 경우
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        }
    }


    @PreAuthorize("hasAuthority('attendance.read')")
    @GetMapping("")
    public ListResponse<AttendanceDto> getAttendanceListByDate(
            @Parameter(name = "date", description = "date=2022-02-11", in = ParameterIn.QUERY)
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<AttendanceDto> attendanceDtoList = attendanceService.getAllAttendanceByDate(
                        date == null ? LocalDate.now() : date)
                .values()
                .stream()
                .collect(Collectors.toList());

        return ListResponse.<AttendanceDto>builder()
                .content(attendanceDtoList)
                .count(attendanceDtoList.size())
                .build();
    }



}