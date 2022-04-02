package com.larry.fc.finalproject.api.dto.tododto;

import com.larry.fc.finalproject.core.domain.entity.Todo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AllUserTodoDto {
    @Schema(name = "writer_id", example = "user id = 1 or 2..")
    private final Long writer_id;
    @Schema(description = "참여자 이름" , example = "진성대")
    private final String userName;
    private final List<TodoDto> todoDtoList;
}
