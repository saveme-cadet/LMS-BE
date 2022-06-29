package com.savelms.api.todo.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class createTodoRequest {

    @Schema(description = "오늘 공부할 것" , example = "공부하기")
    @Size(min = 1, max = 100)
    private final String title;

    @Schema(description = "공부한거 체크" , example = "true or false")
    private final boolean titleCheck;

    @Schema(description = "todo 오늘 요일" , example = "date=2022-02-10")
    private final LocalDate todoDay;
}
