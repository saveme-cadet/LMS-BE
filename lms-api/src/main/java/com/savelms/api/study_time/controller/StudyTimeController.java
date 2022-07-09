package com.savelms.api.study_time.controller;

import com.savelms.api.study_time.dto.StudyTimeResponse;
import com.savelms.api.study_time.dto.UpdateStudyTimeRequest;
import com.savelms.api.study_time.exception.ExceptionResponse;
import com.savelms.api.study_time.exception.StudyTimeNotFoundException;
import com.savelms.api.study_time.service.StudyTimeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudyTimeController {

    private final StudyTimeService studyTimeService;


    /**
     * 생성
     * */
    @Operation(description = "스터디 시작")
    @PostMapping("/study_times")
    public ResponseEntity<StudyTimeResponse> startStudy(@RequestParam(name = "userId") Long userId) {
        StudyTimeResponse studyTime = studyTimeService.startStudy(userId);

        return ResponseEntity.ok().body(studyTime);
    }


    /**
     * 조회
     * */
    @Operation(description = "전체 날짜 스터디 조회")
    @GetMapping("/study_times")
    public ResponseEntity<List<StudyTimeResponse>> getStudyTimes(@RequestParam Long userId) {
        List<StudyTimeResponse> studyTime = studyTimeService.getStudyTimes(userId);

        return ResponseEntity.ok().body(studyTime);
    }

    @Operation(description = "당일 스터디 조회")
    @GetMapping("/study_times/today")
    public ResponseEntity<List<StudyTimeResponse>> getTodayStudyTimes(@RequestParam Long userId) {
        List<StudyTimeResponse> studyTime = studyTimeService.getTodayStudyTimes(userId);

        return ResponseEntity.ok().body(studyTime);
    }

    @Operation(description = "특정 날짜 스터디 조회")
    @GetMapping("/study_times/{createDate}") //createDate : 'yyyy-MM-dd' 포맷
    public ResponseEntity<List<StudyTimeResponse>> getStudyTimesByDate(@RequestParam Long userId,
                                                                       @PathVariable String createDate) throws ParseException {
        List<StudyTimeResponse> studyTime = studyTimeService.getStudyTimesByDate(userId, createDate);

        return ResponseEntity.ok().body(studyTime);
    }


    /**
     * 수정
     * */
    @Operation(description = "스터디 종료")
    @PutMapping("/study_times")
    public ResponseEntity<List<StudyTimeResponse>> endStudy(@RequestParam Long userId) {
        List<StudyTimeResponse> studyTimeResponses = studyTimeService.endStudy(userId);

        return ResponseEntity.ok().body(studyTimeResponses);
    }

    @Operation(description = "스터디 시간 수정")
    @PatchMapping("/study_times/{studyTimeId}")
    public ResponseEntity<StudyTimeResponse> updateStudyTime(@PathVariable Long studyTimeId,
                                          @RequestBody UpdateStudyTimeRequest request) {
        StudyTimeResponse studyTimeResponse = studyTimeService.updateStudyTime(studyTimeId, request);

        return ResponseEntity.ok().body(studyTimeResponse);
    }


    /**
     * 삭제
     * */
    @Operation(description = "스터디 삭제")
    @DeleteMapping("/study_times/{studyTimeId}")
    public ResponseEntity deleteStudyTime(@PathVariable Long studyTimeId) {
        studyTimeService.deleteStudyTime(studyTimeId);

        return ResponseEntity.ok().build();
    }



    /**
     * 예외처리
     * 임시로 API 컨트롤로에서 처리
     * 나중에 ControllerAdvice에서 처리하도록 변경
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

    @ExceptionHandler(StudyTimeNotFoundException.class)
    public ResponseEntity<String> handle(StudyTimeNotFoundException e, HttpServletRequest request) {
        log.error(e.getClass().getName(), e.getMessage());

        String exceptionDir = e.getClass().getName();
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        exceptionResponse.setTimeStamp(LocalDateTime.now());
        exceptionResponse.setMessage("날짜 포맷이 맞지 않습니다.");
        exceptionResponse.setException(exceptionDir.substring(exceptionDir.lastIndexOf(".") + 1));
        exceptionResponse.setPath(request.getRequestURI());

        return new ResponseEntity(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

}
