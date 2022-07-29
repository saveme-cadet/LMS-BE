package com.savelms.api.user.controller.dto;

import com.savelms.core.team.TeamEnum;
import com.savelms.core.user.AttendStatus;
import com.savelms.core.user.role.RoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponseDto {

    @NotNull
    private final String id;
    @Schema(description = "참여 여부" , example = "PARTICIPATED, NOT_PARTICIPATED")
    private final AttendStatus attendStatus;
    @Schema(description = "참여자 이름" , example = "sjin")
    private final String nickname;
    @Schema(description = "역할" , example = "UNAUTHORIZED, USER, MANAGER, ADMIN;")
    private final RoleEnum role;            // 주 마다

    @Schema(description = "이번 주 팀", example = "RED, BLUE, NONE")
    private final TeamEnum team;            // 주 마다
    @Schema(description = "가진 휴가 일 수", example = "1, 2")
    private final Long vacation;
}
