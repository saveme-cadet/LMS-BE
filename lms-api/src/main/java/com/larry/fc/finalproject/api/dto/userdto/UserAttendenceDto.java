package com.larry.fc.finalproject.api.dto.userdto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAttendenceDto {
    @Schema(description = "유저 index" , example = "1 or 2 ..")
    private final Long user_id;
    @Schema(description = "유처 참여 여부", example = "1 or 0")
    private final Long attendStatus;
}
