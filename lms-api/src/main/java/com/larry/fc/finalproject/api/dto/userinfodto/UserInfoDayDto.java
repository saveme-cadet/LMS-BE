package com.larry.fc.finalproject.api.dto.userinfodto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserInfoDayDto {
    @Schema(description = "userId")
    @NotNull
    @Min(1)
    private final Long write_id;
    @Schema(description = "참여자 이름" , example = "sjin")
    private final String userName;
    @Schema(description = "역할" , example = "카 뎃 or 머슴")
    private final String role;            // 주 마다
    @Schema(description = "참석 점수", example = "2.4")
    private final double attendScore;     // 매일 자동 갱신
    @Schema(description = "이번 주 팀", example = "red or blue")
    private final String team;            // 주 마다
    @Schema(description = "level" , example = "구해줘 카뎃 참여한 총 달")
    private final int level;
}
