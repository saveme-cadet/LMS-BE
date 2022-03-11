package com.larry.fc.finalproject.api.dto.tododto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class TodoDto {
    @Schema(name= "userId")
    @NotNull
    @Min(1)
    private final Long userId;
    @Schema(description = "todo id" , example = "1. 공부하기 등등")
    @NotNull
    @Min(1)
    private final Long todoId;
    @Schema(description = "오늘 공부할 것" , example = "공부하기")
    @Size(min = 1, max = 100)
    private final String title;
    @Schema(description = "공부한거 체크" , example = "true or false")
    private final boolean titleCheck;
    @Schema(description = "todo 오늘 요일" , example = "date=2022-02-10")
    private final LocalDate todoDay;
}
