package com.larry.fc.finalproject.api.dto.userinfodto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRoleChangeDto {
    @Schema(description = "유저 index" , example = "1 or 2 ..")
    private final Long user_id;
    @Schema(description = "역할" , example = "카 뎃 or 머슴")
    private final String role;
}
