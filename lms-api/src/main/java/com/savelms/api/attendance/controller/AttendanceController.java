package com.savelms.api.attendance.controller;

import com.savelms.api.attendance.service.AttendanceService;
import com.savelms.core.attendance.dto.CheckIOReq;
import com.savelms.core.exception.NoPermissionException;
import com.savelms.core.user.domain.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
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
    private final UserRepository userRepository;


    @PatchMapping("/{attendanceId}/checkin")
    public ResponseEntity<String> userCheckIn(
            @AuthenticationPrincipal User sessionUser,
            @PathVariable("attendanceId") Long attendanceId,
            @Valid @RequestBody CheckIOReq reqBody) {
        /**
         * 로그인 했는지 확인하기 -> 시큐리티
         * 바디 파라미터 형식 확인하기 -> 컨트롤러 @Valid
         * 권한 확인하기 -> 서비스 Exceptions
         * 출결표 있는지 확인하기 -> 서비스
         */

        try {
            //유저 찾은 후, 정상적으로 유저를 찾으면 비즈니스 로직 실행
            userRepository.findByUsername(sessionUser.getUsername())
                    .ifPresent(user -> attendanceService.checkIn(attendanceId, user, reqBody.getStatus()));
            //정상적으로 로직이 실행 된 경우
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } catch (NoPermissionException e) {
            //변경 권한이 없는 유저가 변경을 시도한 경우
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            //잘못된 출결표Id로 요청한 경우
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PatchMapping("/{attendanceId}/checkout")
    public ResponseEntity<String> userCheckOut(
            @AuthenticationPrincipal User sessionUser,
            @PathVariable("attendanceId") Long attendanceId,
            @Valid @RequestBody CheckIOReq reqBody) {
        /**
         * 로그인 했는지 확인하기 -> 시큐리티
         * 바디 파라미터 형식 확인하기 -> 컨트롤러 @Valid
         * 권한 확인하기 -> 서비스 Exceptions
         * 출결표 있는지 확인하기 -> 서비스
         */

        try {
            //유저 찾은 후, 정상적으로 유저를 찾으면 비즈니스 로직 실행
            userRepository.findByUsername(sessionUser.getUsername())
                    .ifPresent(user -> attendanceService.checkOut(attendanceId, user, reqBody.getStatus()));
            //정상적으로 로직이 실행 된 경우
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } catch (NoPermissionException e) {
            //변경 권한이 없는 유저가 변경을 시도한 경우
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            //체크인을 하지 않고 체크아웃을 시도한 경우
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            //잘못된 출결표Id로 요청한 경우
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


//    @GetMapping("?date=")
//    public List<AttendanceTableDto> dateAttendance(
//            @RequestParam(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate refDt) {
//
//
//
//        //일단 캘린더를 찾고, 참가하는 유저들을 찾아
//        //캘린더로 통계(어제꺼 봐야함 -> 점수 합산용), 출결, 투두, 아오지를 가져와
//        //유저를 기준으로 합쳐서
//        //유저로는 팀, 역할, 이름, 휴가(통계 불필요)
//        //출결로는 출석-결석 점수(통계 필요), 체크-인아웃(통계 불필요)
//        //투두로는 진척도(통계 불필요)
//        //아오지는 아오지 시간(통계 필요)
//
//    }
}
