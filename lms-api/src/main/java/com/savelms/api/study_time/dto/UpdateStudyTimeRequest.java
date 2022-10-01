package com.savelms.api.study_time.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
public class UpdateStudyTimeRequest {

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime testBeginTime;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime testEndTime;

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
