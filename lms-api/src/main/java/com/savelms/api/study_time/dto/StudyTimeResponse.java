package com.savelms.api.study_time.dto;

import com.savelms.core.study_time.domain.entity.StudyTime;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
public class StudyTimeResponse {

    private String beginTime;
    private String endTime;
    private String finalStudyTime;

    public StudyTimeResponse(StudyTime studyTime) {
        this.beginTime = DateTimeFormatter.ofPattern(StudyTime.TIME_FORMAT).format(studyTime.getBeginTime());
        this.endTime = DateTimeFormatter.ofPattern(StudyTime.TIME_FORMAT).format(studyTime.getEndTime());
        this.finalStudyTime = studyTime.getFinalStudyTime();
    }

}
