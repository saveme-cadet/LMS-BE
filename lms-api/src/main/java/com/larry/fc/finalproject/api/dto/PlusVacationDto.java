package com.larry.fc.finalproject.api.dto;

import com.larry.fc.finalproject.core.domain.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Builder
@Data
public class PlusVacationDto {
    @Schema(name = "userId" , example = "userId = 1")
    @NotNull
    @Min(1)
    private final Long userId;
    @Schema(description = "머슴 시작 날짜" , example = "date=2022-02-17")
    private final LocalDate startAt;
    @Schema(description = "머슴 끝난 날짜" , example = "date=2022-02-24")
    private final LocalDate endAt;
    @Schema(description = "머슴 끝난 여부" , example = "false")
    private final boolean finished;
}
