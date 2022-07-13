package com.savelms.api.todo.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTodoRequest {
    @Schema(description = "오늘 공부할 것" , example = "공부하기")
    @Size(min = 1, max = 100)
    private String title;
    @Schema(description = "공부한거 체크" , example = "true or false")
    private Boolean titleCheck;
}
