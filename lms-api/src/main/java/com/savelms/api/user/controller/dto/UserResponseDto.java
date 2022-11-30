package com.savelms.api.user.controller.dto;

import com.savelms.core.team.TeamEnum;
import com.savelms.core.user.AttendStatus;
import com.savelms.core.user.role.RoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponseDto {

    @Schema(name= "userId" , example = "db688a4a-2f70-4265-a1ea-d15fd6c5c914")
    @NotBlank
    private final String id;

    @Schema(description = "참여 여부" , example = "PARTICIPATED, NOT_PARTICIPATED")
    @NotNull
    private final AttendStatus attendStatus;

    @Schema(description = "참여자 이름" , example = "sjin")
    @NotBlank
    private final String nickname;

    @Schema(description = "역할" , example = "UNAUTHORIZED, USER, MANAGER, ADMIN")
    @NotNull
    private final RoleEnum role;            // 주 마다

    @Schema(description = "이번 주 팀", example = "RED, BLUE, NONE")
    @NotNull
    private final TeamEnum team;
    // 주 마다
    @Schema(description = "가진 휴가 일 수", example = "1, 2")
    @NotNull
    private final Double vacation;

    @Schema(description = "출석 점수", example = "1, 2")
    @NotNull
    private final Double attendanceScore;

    @Schema(description = "총 결석 점수(결석 점수 - 아오지 점수)", example = "1, 2")
    @NotNull
    private final Double totalScore;

    @Schema(description = "이 번주 결석 점수", example = "1, 2")
    @NotNull
    private final Double weekAbsentScore;
}
