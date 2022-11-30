package com.savelms.api.study_time.controller;

import com.savelms.api.commondata.APIDataResponse;
import com.savelms.api.study_time.dto.StudyTimeResponse;
import com.savelms.api.study_time.dto.StudyingUserResponse;
import com.savelms.api.study_time.dto.UpdateStudyTimeRequest;
import com.savelms.api.study_time.service.StudyTimeService;
import com.savelms.core.study_time.domain.entity.StudyTime;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "아오지 API")
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudyTimeController {

    private final StudyTimeService studyTimeService;

    @PreAuthorize("hasAuthority('user.study-time.create')")
    @Operation(description = "스터디 시작", summary = "스터디 시작")
    @PostMapping("/users/{userId}/study_times")
    public APIDataResponse<ResponseEntity<StudyTimeResponse>> startStudy(@PathVariable String userId) {
        StudyTimeResponse studyTime = studyTimeService.createStudyTime(userId);

        return APIDataResponse.of(ResponseEntity.ok().body(studyTime));
    }


    @PreAuthorize("hasAuthority('user.study-time.read')")
    @Operation(description = "개인유저의 당일 스터디 기록 조회", summary = "개인유저의 당일 스터디 기록 조회")
    @GetMapping("/users/{userId}/study_times/today")
    public APIDataResponse<List<StudyTimeResponse>> getTodayStudyTimes(@PathVariable String userId) {
        List<StudyTimeResponse> studyTime = studyTimeService.getTodayStudyTimes(userId);

        return APIDataResponse.of(studyTime);
    }

    @PreAuthorize("hasAuthority('user.study-time.read')")
    @Operation(description = "개인유저의 특정 날짜 스터디 기록 조회", summary = "개인유저의 특정 날짜 스터디 기록 조회")
    @GetMapping("/users/{userId}/study_times/{date}")
    public APIDataResponse<List<StudyTimeResponse>> getStudyTimesByDate(
            @PathVariable String userId,
            @PathVariable @DateTimeFormat(pattern = StudyTime.DATE_FORMAT) LocalDate date
    ) {

        List<StudyTimeResponse> studyTime = studyTimeService.getStudyTimesByDate(userId, date);

        return APIDataResponse.of(studyTime);
    }

    @PreAuthorize("hasAuthority('user.study-time.read')")
    @Operation(description = "개인유저의 모든 스터디 기록 조회", summary = "개인유저의 모든 스터디 기록 조회")
    @GetMapping("/users/{userId}/study_times")
    public APIDataResponse<List<StudyTimeResponse>> getStudyTimes(@PathVariable String userId) {
        List<StudyTimeResponse> studyTime = studyTimeService.getStudyTimes(userId);

        return APIDataResponse.of(studyTime);
    }

    @PreAuthorize("hasAuthority('study-time.user.read')")
    @Operation(description = "현재 스터디 중인 전체 회원 조회", summary = "현재 스터디 중인 전체 회원 조회")
    @GetMapping("/users/{userId}/study_times/studying-user")
    public APIDataResponse<List<StudyingUserResponse>> getStudyingUser(@PathVariable String userId) {
        List<StudyingUserResponse> studyingUserResponse = studyTimeService.getStudyingUser(userId);

        return APIDataResponse.of(studyingUserResponse);
    }


    @PreAuthorize("hasAuthority('user.study-time.update')")
    @Operation(description = "스터디 종료", summary = "스터디 종료")
    @PutMapping("/users/{userId}/study_times")
    public APIDataResponse<ResponseEntity<StudyTimeResponse>> endStudy(@PathVariable String userId) {
        StudyTimeResponse studyTimeResponse = studyTimeService.endStudy(userId);

        return APIDataResponse.of(ResponseEntity.ok().body(studyTimeResponse));
    }


    @PreAuthorize("hasAuthority('study-time.update')")
    @Operation(description = "스터디 시간 수정", summary = "스터디 시간 수정")
    @PatchMapping("/users/{userId}/study_times/{studyTimeId}")
    public APIDataResponse<ResponseEntity<StudyTimeResponse>> updateStudyTime(@PathVariable String userId,
                                                             @PathVariable Long studyTimeId,
                                                             @Valid @RequestBody UpdateStudyTimeRequest request) {
        StudyTimeResponse studyTimeResponse = studyTimeService.updateStudyTime(userId, studyTimeId, request);

        return APIDataResponse.of(ResponseEntity.ok().body(studyTimeResponse));
    }


    @PreAuthorize("hasAuthority('study-time.delete')")
    @Operation(description = "스터디 삭제", summary = "스터디 삭제")
    @DeleteMapping("/users/{userId}/study_times/{studyTimeId}")
    public APIDataResponse<ResponseEntity<Void>> deleteStudyTime(@PathVariable String userId,
                                                @PathVariable Long studyTimeId) {
        studyTimeService.deleteStudyTime(userId, studyTimeId);

        return APIDataResponse.of(ResponseEntity.ok().build());
    }
}
