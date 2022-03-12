package com.larry.fc.finalproject.api.dto.tododto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class RequestTodoDto {
    @Schema(name = "todoId" , example = "1")
    @NotNull
    @Min(1)
    private final Long writerId;
    @Schema(description = "todo 오늘 요일" , example = "date=2022-02-10")
    private final LocalDate date;
}
