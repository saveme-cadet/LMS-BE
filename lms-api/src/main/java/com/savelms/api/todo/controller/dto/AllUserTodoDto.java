package com.savelms.api.todo.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AllUserTodoDto {
    @Schema(name = "writer_id", example = "4f3dda35-3739-406c-ad22-eed438831d66")
    private final String writerId;
    @Schema(description = "참여자 이름" , example = "진성대")
    private final String username;
    private final List<AllUserTodoSingleTodoDto> todoDtoList;
}