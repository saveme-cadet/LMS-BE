package com.savelms.api.study_time.dto;

import com.savelms.core.study_time.domain.entity.StudyTime;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
public class StudyTimeResponse {

    private Long studyTimeId;
    private String createdDate;
    private String beginTime;
    private String endTime;
    private String finalStudyTime;
    private Double studyTimeScore;

    public StudyTimeResponse(StudyTime studyTime) {
        this.studyTimeId = studyTime.getId();
        this.createdDate = DateTimeFormatter.ofPattern(StudyTime.CREATED_DATE_FORMAT).format(studyTime.getCreatedAt());
        this.beginTime = DateTimeFormatter.ofPattern(StudyTime.TIME_FORMAT).format(studyTime.getBeginTime());
        this.endTime = DateTimeFormatter.ofPattern(StudyTime.TIME_FORMAT).format(studyTime.getEndTime());
        this.finalStudyTime = studyTime.getFinalStudyTime();
        this.studyTimeScore = StudyTime.getStudyScore(studyTime.getBeginTime(), studyTime.getEndTime());
    }

}
