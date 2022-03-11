package com.larry.fc.finalproject.api.dto.tododto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@Validated
public class DeleteTodoDto {
    @Schema(name= "writerId")
    @NotNull
    @Min(1)
    private final Long writerId;
    @Schema(name = "todoId" , example = "1. 공부하기 등등")
    @NotNull
    @Min(1)
    private final Long todoId;
    @Schema(description = "todo 오늘 요일" , example = "date=2022-02-10")
    private final LocalDate todoDay;
}
