package com.larry.fc.finalproject.api.dto.tabledto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class TableCheckOutDto {
    @Schema(description = "userId")
    @NotNull
    @Min(1)
    private final Long id;
    @Schema(description = "체크 아웃" , example = "출석 = 1, 지각 = 2 ..")
    @Min(0)
    private final short checkOut;
    @Schema(description = "찾고자 하는 요일" , example = "date=2022-02-10")
    private LocalDate tableDay;
}
