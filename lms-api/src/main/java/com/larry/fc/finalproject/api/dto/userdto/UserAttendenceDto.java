package com.larry.fc.finalproject.api.dto.userdto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAttendenceDto {
    @Schema(name = "userId" , example = "1")
    private final Long userId;
    @Schema(description = "유처 참여 여부", example = "1L")
    private final Long attendStatus;
}
