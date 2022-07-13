package com.savelms.api.todo.controller.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateTodoResponse {

    private Long todoId;
}
