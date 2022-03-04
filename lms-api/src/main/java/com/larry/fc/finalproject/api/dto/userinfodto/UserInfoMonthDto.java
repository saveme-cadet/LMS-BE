package com.larry.fc.finalproject.api.dto.userinfodto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Builder
@Data
public class UserInfoMonthDto {
    @Schema(description = "userId")
    @NotNull
    @Min(1)
    private final Long id;
    @Schema(description = "level" , example = "구해줘 카뎃 참여한 총 달")
    private final int level;
    @Schema(description = "지금 진행 중인 과제" , example = "libft")
    private final String nowSubject;
    @Schema(description = "현재 자신있는 과제" , example = "libft")
    private final String confidenceSubject;
    @Schema(description = "참여 여부" , example = "1 or 2")
    private final Long attendeStatus;
}
