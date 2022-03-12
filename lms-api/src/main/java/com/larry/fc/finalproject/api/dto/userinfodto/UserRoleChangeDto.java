package com.larry.fc.finalproject.api.dto.userinfodto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRoleChangeDto {
    @Schema(name = "userId" , example = "1")
    private final Long userId;
    @Schema(description = "역할" , example = "카 뎃 or 머슴")
    private final String role;
}
