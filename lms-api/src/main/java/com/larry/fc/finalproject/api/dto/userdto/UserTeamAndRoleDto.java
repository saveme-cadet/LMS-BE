package com.larry.fc.finalproject.api.dto.userdto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserTeamAndRoleDto {
    @Schema(name = "userId" , example = "1")
    private final Long userId;
    @Schema(description = "유처 팀", example = "red")
    private final String team;
    @Schema(description = "유저 역할", example = "카 뎃")
    private final String role;
}
