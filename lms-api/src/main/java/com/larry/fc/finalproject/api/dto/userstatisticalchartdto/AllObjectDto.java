package com.larry.fc.finalproject.api.dto.userstatisticalchartdto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
public class AllObjectDto {
    @Schema(description = "일간 목표 달성률" , example = "70.0")
    @Min(0)
    @Max(100)
    private double dayObjectiveAchievementRate;
    @Schema(description = "월간 목표 달성률" , example = "70.0")
    @Min(0)
    @Max(100)
    private double monthObjectiveAchievementRate;
}
