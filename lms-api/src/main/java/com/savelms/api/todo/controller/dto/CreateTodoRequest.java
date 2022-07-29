package com.savelms.api.todo.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateTodoRequest {

    @Schema(description = "오늘 공부할 것" , example = "공부하기")
    @NotBlank
    @Size(min = 1, max = 100)
    private String title;


    @Schema(description = "todo 오늘 요일" , example = "date=2022-02-10")
    @NotNull
    private LocalDate todoDay;
}
