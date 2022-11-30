package com.savelms.api.study_time.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
public class UpdateStudyTimeRequest {

    @NotNull
    private LocalDateTime beginTime;

    @NotNull
    private LocalDateTime endTime;

    protected UpdateStudyTimeRequest() {}

    public UpdateStudyTimeRequest(LocalDateTime beginTime, LocalDateTime endTime) {
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    public UpdateStudyTimeRequest of(LocalDateTime beginTime, LocalDateTime endTime) {
        return new UpdateStudyTimeRequest(beginTime, endTime);
    }
}
