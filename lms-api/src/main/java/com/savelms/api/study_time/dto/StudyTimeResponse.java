package com.savelms.api.study_time.dto;

import com.savelms.core.study_time.domain.entity.StudyTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class StudyTimeResponse {

    private Long studyTimeId;
    private String createdDate;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private String finalStudyTime;
    private Double studyTimeScore;

    private StudyTimeResponse(
            Long studyTimeId,
            String createdDate,
            LocalDateTime beginTime,
            LocalDateTime endTime,
            String finalStudyTime,
            Double studyTimeScore)
    {
        this.studyTimeId = studyTimeId;
        this.createdDate = createdDate;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.finalStudyTime = finalStudyTime;
        this.studyTimeScore = studyTimeScore;
    }

    public static StudyTimeResponse from(StudyTime studyTime) {
        return new StudyTimeResponse(
                studyTime.getId(),
                DateTimeFormatter.ofPattern(StudyTime.DATE_FORMAT).format(studyTime.getBeginTime()),
                studyTime.getBeginTime(),
                studyTime.getEndTime(),
                studyTime.getFinalStudyTime(),
                StudyTime.getStudyScore(studyTime.getBeginTime(), studyTime.getEndTime()));
    }
}
