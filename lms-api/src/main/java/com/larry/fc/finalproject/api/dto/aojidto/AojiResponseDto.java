package com.larry.fc.finalproject.api.dto.aojidto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class AojiResponseDto {
    @Schema(name = "userId", example = "1")
    @NotNull
    @Min(1)
    private final Long userId;
    @Schema(description = "user aoji index", example = "1, 2,..")
    private final Long aojiTimeIndex;
    @Schema(description = "시작 시간", example = "2022-03-12 02:38:10.268987")
    private final LocalDateTime startAt;
    @Schema(description = "끝나는 시간", example = "2022-03-12 02:38:33.488874")
    private final LocalDateTime endAt;
    @Schema(description = "시간 차", example = "03:00:00")
    private final String recodeTime;
}
