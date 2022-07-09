package com.savelms.api.study_time.dto;

import com.savelms.core.study_time.domain.entity.StudyTime;
import lombok.Data;

import java.time.format.DateTimeFormatter;


@Data
public class StudyingUserResponse {

    private String name;
    private String team;
    private String beginTime;

    public StudyingUserResponse(StudyTime studyTime) {
        this.name = studyTime.getUser().getUsername();
//        this.team = studyTime.getUser().getUserTeam().getTeam().getTeamEnum().name();
        this.beginTime = DateTimeFormatter.ofPattern(StudyTime.TIME_FORMAT).format(studyTime.getBeginTime());
    }

}
