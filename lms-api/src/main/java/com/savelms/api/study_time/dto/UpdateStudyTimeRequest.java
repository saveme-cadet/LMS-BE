package com.savelms.api.study_time.dto;

import lombok.Data;

@Data
public class UpdateStudyTimeRequest {

    private String beginTime;
    private String endTime;

}
