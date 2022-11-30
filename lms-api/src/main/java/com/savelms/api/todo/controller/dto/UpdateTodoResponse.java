package com.savelms.api.todo.controller.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateTodoResponse {

    @Schema(name = "todoId" , example = "1")
    @NotNull
    private Long todoId;
}