package com.savelms.api.todo.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AllUserTodoSingleTodoDto {

    @Schema(name= "writerId" , example = "4f3dda35-3739-406c-ad22-eed438831d66")
    @NotBlank
    private final String writerId;

    @Schema(name = "todoId" , example = "1")
    @NotNull
    private final Long todoId;

    @Schema(description = "오늘 공부할 것" , example = "공부하기")
    @NotBlank
    @Size(min = 1, max = 100)
    private final String title;

    @Schema(description = "공부한거 체크" , example = "true or false")
    @NotNull
    private final Boolean titleCheck;

    @Schema(description = "todo 오늘 요일" , example = "date=2022-02-10")
    @NotNull
    private final LocalDate todoDay;
}
