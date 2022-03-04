package com.larry.fc.finalproject.api.dto.userinfodto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserVacationChangeDto {
    @Schema(description = "유저 index", example = "1, 2, 3,..")
    private final Long userid;
    private final boolean check;
}
