package com.savelms.api.study_time.controller;

import com.savelms.api.study_time.dto.StudyTimeResponse;
import com.savelms.api.study_time.dto.StudyingUserResponse;
import com.savelms.api.study_time.dto.UpdateStudyTimeRequest;
import com.savelms.core.exception.ExceptionResponse;
import com.savelms.core.exception.StudyTimeNotFoundException;
import com.savelms.api.study_time.service.StudyTimeService;
import com.savelms.core.study_time.domain.entity.StudyTime;
import com.savelms.core.user.domain.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Tag(name = "아오지 API")
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudyTimeController {

    private final StudyTimeService studyTimeService;


    /**
     * 생성
     * */
//    @PreAuthorize("hasAuthority('user.study-time.create')")
    @Operation(description = "스터디 시작", summary = "스터디 시작")
    @PostMapping("/users/{userId}/study_times")
    public ResponseEntity<StudyTimeResponse> startStudy(@PathVariable String userId) {
        StudyTimeResponse studyTime = studyTimeService.startStudy(userId);

        return ResponseEntity.ok().body(studyTime);
    }


    /**
     * 조회
     * */
//    @PreAuthorize("hasAuthority('user.study-time.read')")
    @Operation(description = "개인유저의 당일 스터디 기록 조회", summary = "개인유저의 당일 스터디 기록 조회")
    @GetMapping("/users/{userId}/study_times/today")
    public ResponseEntity<List<StudyTimeResponse>> getTodayStudyTimes(@PathVariable String userId) {
        List<StudyTimeResponse> studyTime = studyTimeService.getTodayStudyTimes(userId);

        return ResponseEntity.ok().body(studyTime);
    }

//    @PreAuthorize("hasAuthority('user.study-time.read')")
    @Operation(description = "개인유저의 특정 날짜 스터디 기록 조회", summary = "개인유저의 특정 날짜 스터디 기록 조회")
    @GetMapping("/users/{userId}/study_times/{date}")
    public ResponseEntity<List<StudyTimeResponse>> getStudyTimesByDate(
            @PathVariable String userId,
            @PathVariable @DateTimeFormat(pattern = StudyTime.DATE_FORMAT) LocalDate date
    ) {

        List<StudyTimeResponse> studyTime = studyTimeService.getStudyTimesByDate(userId, date);

        return ResponseEntity.ok().body(studyTime);
    }

//    @PreAuthorize("hasAuthority('user.study-time.read')")
    @Operation(description = "개인유저의 모든 스터디 기록 조회", summary = "개인유저의 모든 스터디 기록 조회")
    @GetMapping("/users/{userId}/study_times")
    public ResponseEntity<List<StudyTimeResponse>> getStudyTimes(@PathVariable String userId) {
        List<StudyTimeResponse> studyTime = studyTimeService.getStudyTimes(userId);

        return ResponseEntity.ok().body(studyTime);
    }

//    @PreAuthorize("hasAuthority('study-time.user.read')")
    @Operation(description = "현재 스터디 중인 전체 회원 조회", summary = "현재 스터디 중인 전체 회원 조회")
    @GetMapping("/users/{userId}/study_times/studying-user")
    public ResponseEntity<List<StudyingUserResponse>> getStudyingUser(@PathVariable String userId) {
        List<StudyingUserResponse> studyingUserResponse = studyTimeService.getStudyingUser(userId);

        return ResponseEntity.ok().body(studyingUserResponse);
    }


    /**
     * 수정
     * */
//    @PreAuthorize("hasAuthority('user.study-time.update')")
    @Operation(description = "스터디 종료", summary = "스터디 종료")
    @PutMapping("/users/{userId}/study_times")
    public ResponseEntity<StudyTimeResponse> endStudy(@PathVariable String userId) {
        StudyTimeResponse studyTimeResponse = studyTimeService.endStudy(userId);

        return ResponseEntity.ok().body(studyTimeResponse);
    }

//    @PreAuthorize("hasAuthority('study-time.update')")
    @Operation(description = "스터디 시간 수정", summary = "스터디 시간 수정")
    @PatchMapping("/users/{userId}/study_times/{studyTimeId}")
    public ResponseEntity<StudyTimeResponse> updateStudyTime(@PathVariable String userId,
                                                             @PathVariable Long studyTimeId,
                                                             @RequestBody UpdateStudyTimeRequest request) {
        StudyTimeResponse studyTimeResponse = studyTimeService.updateStudyTime(userId, studyTimeId, request);

        return ResponseEntity.ok().body(studyTimeResponse);
    }


    /**
     * 삭제
     * */
//    @PreAuthorize("hasAuthority('study-time.delete')")
    @Operation(description = "스터디 삭제", summary = "스터디 삭제")
    @DeleteMapping("/users/study_times/{studyTimeId}")
    public ResponseEntity deleteStudyTime(@PathVariable Long studyTimeId) {
        studyTimeService.deleteStudyTime(studyTimeId);

        return ResponseEntity.ok().build();
    }



    /**
     * 예외처리
     * TODO: 임시로 API 컨트롤러 클래스에서 처리, 나중에 ControllerAdvice 만들고 거기에서 처리하도록 변경
     * */
    @ExceptionHandler(ParseException.class)
    public ResponseEntity<String> handle(ParseException e, HttpServletRequest request) {
        log.error(e.getClass().getName(), e.getMessage());

        String exceptionDir = e.getClass().getName();
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        exceptionResponse.setTimeStamp(LocalDateTime.now());
        exceptionResponse.setMessage("날짜 포맷이 맞지 않습니다.");
        exceptionResponse.setException(exceptionDir.substring(exceptionDir.lastIndexOf(".") + 1));
        exceptionResponse.setPath(request.getRequestURI());

        return new ResponseEntity(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handle(EntityNotFoundException e, HttpServletRequest request) {
        log.error(e.getClass().getName(), e.getMessage());

        String exceptionDir = e.getClass().getName();
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        exceptionResponse.setTimeStamp(LocalDateTime.now());
        exceptionResponse.setMessage(e.getMessage());
        exceptionResponse.setException(exceptionDir.substring(exceptionDir.lastIndexOf(".") + 1));
        exceptionResponse.setPath(request.getRequestURI());

        return new ResponseEntity(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(StudyTimeNotFoundException.class)
    public ResponseEntity<String> handle(StudyTimeNotFoundException e, HttpServletRequest request) {
        log.error(e.getClass().getName(), e.getMessage());

        String exceptionDir = e.getClass().getName();
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        exceptionResponse.setTimeStamp(LocalDateTime.now());
        exceptionResponse.setMessage(e.getMessage());
        exceptionResponse.setException(exceptionDir.substring(exceptionDir.lastIndexOf(".") + 1));
        exceptionResponse.setPath(request.getRequestURI());

        return new ResponseEntity(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }
}
