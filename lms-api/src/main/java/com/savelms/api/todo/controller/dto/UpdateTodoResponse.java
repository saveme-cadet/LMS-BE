package com.savelms.api.todo.controller.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateTodoResponse {

    private Long todoId;
}