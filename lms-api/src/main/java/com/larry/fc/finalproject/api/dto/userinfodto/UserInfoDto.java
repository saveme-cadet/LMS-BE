package com.larry.fc.finalproject.api.dto.userinfodto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class UserInfoDto {
    @Schema(name = "userId")
    @NotNull
    @Min(1)
    private final Long userId;
    @Schema(description = "level" , example = "구해줘 카뎃 참여한 총 달")
    private final int level;
    @Schema(description = "지금 진행 중인 과제" , example = "libft")
    private final String nowSubject;
    @Schema(description = "현재 자신있는 과제" , example = "libft")
    private final String confidenceSubject;
    @Schema(description = "참여 여부" , example = "1 or 2")
    private final Long attendeStatus;
    @Schema(description = "참여자 이름" , example = "sjin")
    private final String userName;
    @Schema(description = "역할" , example = "카 뎃 or 머슴")
    private final String role;            // 주 마다
    @Schema(description = "참석 점수", example = "2.4")
    private final double attendScore;     // 매일 자동 갱신
    @Schema(description = "이번 주 팀", example = "red or blue")
    private final String team;            // 주 마다
    @Schema(description = "가진 휴가 일 수", example = "1 or 2 ++")
    private final Double vacation;
    @Schema(description = "아오지 탄광 빼고 출석 일 수", example = "1.2 , 1.5..")
    private final double participateScore;
}

