package com.savelms.api.study_time.dto;

import com.savelms.core.study_time.domain.entity.StudyTime;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
public class StudyTimeResponse {

    private Long studyTimeId;
    private String beginTime;
    private String endTime;
    private String finalStudyTime;
    private Double studyTimeScore;

    public StudyTimeResponse(StudyTime studyTime, Double studyTimeScore) {
        this.studyTimeId = studyTime.getId();
        this.beginTime = DateTimeFormatter.ofPattern(StudyTime.TIME_FORMAT).format(studyTime.getBeginTime());
        this.endTime = DateTimeFormatter.ofPattern(StudyTime.TIME_FORMAT).format(studyTime.getEndTime());
        this.finalStudyTime = studyTime.getFinalStudyTime();
    }

}
