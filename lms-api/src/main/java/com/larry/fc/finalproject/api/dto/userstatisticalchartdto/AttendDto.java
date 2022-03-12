package com.larry.fc.finalproject.api.dto.userstatisticalchartdto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class AttendDto {
    @Schema(name = "userId" , example = "유저 아이디")
    @NotNull
    @Min(1)
    private Long userId;
    @Schema(description = "월간 출석률" , example = "70.0")
    @Min(0)
    @Max(100)
    private double monthAttendanceRate;
}
