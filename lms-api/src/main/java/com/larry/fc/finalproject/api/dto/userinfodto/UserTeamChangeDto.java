package com.larry.fc.finalproject.api.dto.userinfodto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserTeamChangeDto {
    @Schema(name = "userId" , example = "1 or 2 ..")
    private Long userId;
    @Schema(description = "이번주 팀" , example = "red or blue")
    private String team;
}
