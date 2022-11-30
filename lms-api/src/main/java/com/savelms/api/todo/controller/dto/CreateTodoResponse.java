package com.savelms.api.todo.controller.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateTodoResponse {
    @Schema(name= "todoId" , example = "3")
    @NotNull
    private Long todoId;
}
