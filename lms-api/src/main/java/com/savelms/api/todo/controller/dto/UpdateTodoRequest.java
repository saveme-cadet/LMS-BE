package com.savelms.api.todo.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateTodoRequest {
    @Schema(description = "오늘 공부할 것" , example = "공부하기")
    @Size(min = 1, max = 100)
    private final String title;
    @Schema(description = "공부한거 체크" , example = "true or false")
    private final Boolean titleCheck;
}
