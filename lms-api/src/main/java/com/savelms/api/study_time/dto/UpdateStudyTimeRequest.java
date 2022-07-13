package com.savelms.api.study_time.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateStudyTimeRequest {

    private String beginTime;
    private String endTime;

    public UpdateStudyTimeRequest of(String beginTime, String endTime) {
        return new UpdateStudyTimeRequest(beginTime, endTime);
    }
}
