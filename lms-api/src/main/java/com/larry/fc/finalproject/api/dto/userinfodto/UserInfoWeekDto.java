package com.larry.fc.finalproject.api.dto.userinfodto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
@Data
public class UserInfoWeekDto {
    @Schema(description = "userId")
    @NotNull
    @Min(1)
    private final Long id;
    @Schema(description = "역할" , example = "카 뎃 or 머슴")
    private final String role;            // 주 마다
    @Schema(description = "참석 점수", example = "2.4")
    private final double attendScore;     // 매일 자동 갱신
    @Schema(description = "이번 주 팀", example = "red or blue")
    private final String team;            // 주 마다
    @Schema(description = "가진 휴가 일 수", example = "1 or 2 ++")
    private final Double vacation;
}
