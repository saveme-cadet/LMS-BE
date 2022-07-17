package com.savelms.api.study_time.controller;

import com.savelms.api.study_time.dto.StudyTimeResponse;
import com.savelms.api.study_time.service.StudyTimeService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@Disabled
@DisplayName("StudyTime API 테스트")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(StudyTimeController.class)
class StudyTimeControllerTest {

    private final MockMvc mvc;

    @MockBean
    private StudyTimeService studyTimeService;

    public StudyTimeControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("스터디 시작 및 종료 테스트")
    @Test
    void study_start_and_end_test() {
        // Given
        given(studyTimeService.startStudy("user")).willReturn(ArgumentMatchers.any(StudyTimeResponse.class));

        // When & Then

    }


}