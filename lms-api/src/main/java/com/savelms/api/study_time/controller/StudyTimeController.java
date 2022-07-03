package com.savelms.api.study_time.controller;

import com.savelms.api.study_time.dto.StudyTimeResponse;
import com.savelms.api.study_time.service.StudyTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudyTimeController {

    private final StudyTimeService studyTimeService;


    /**
     * 생성
     * */
    @PostMapping("/study_times")
    public ResponseEntity<StudyTimeResponse> startStudy(@RequestParam(name = "userId") Long userId) {
        StudyTimeResponse studyTime = studyTimeService.startStudy(userId);

        return ResponseEntity.ok().body(studyTime);
    }


    /**
     * 조회
     * */
    @GetMapping("/study_times")
    public ResponseEntity<List<StudyTimeResponse>> getStudyTimes(@RequestParam Long userId) {
        List<StudyTimeResponse> studyTime = studyTimeService.getStudyTimes(userId);

        return ResponseEntity.ok().body(studyTime);
    }


    /**
     * 수정
     * */
    @PutMapping("/study_times")
    public ResponseEntity endStudy(@RequestParam Long userId) {
        studyTimeService.endStudy(userId);

        return ResponseEntity.ok().build();
    }

}
