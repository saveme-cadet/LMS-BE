package com.savelms.api.study_time.dto;

import com.savelms.core.study_time.domain.entity.StudyTime;
import com.savelms.core.team.TeamEnum;
import com.savelms.core.team.domain.entity.UserTeam;
import lombok.Data;

import java.time.format.DateTimeFormatter;


@Data
public class StudyingUserResponse {

    private String name;
    private TeamEnum team;
    private String beginTime;

    public StudyingUserResponse(StudyTime studyTime) {
        this.name = studyTime.getUser().getUsername();
        this.team = studyTime.getUser().getUserTeams().stream()
                .filter(UserTeam::getCurrentlyUsed)
                .map(ut -> ut.getTeam().getValue())
                .findAny().orElse(TeamEnum.NONE);
        this.beginTime = DateTimeFormatter.ofPattern(StudyTime.TIME_FORMAT).format(studyTime.getBeginTime());
    }
}
