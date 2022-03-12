package com.larry.fc.finalproject.api.dto.aojidto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AojiDto {
    @Schema(description = "user aoji index", example = "1")
    private final Long aojiTimeIndex;
    @Schema(description = "시작 시간", example = "2022-03-12 02:38:10.268987")
    private final LocalDateTime startAt;
    @Schema(description = "끝나는 시간", example = "2022-03-12 02:38:33.488874")
    private final LocalDateTime endAt;
}
