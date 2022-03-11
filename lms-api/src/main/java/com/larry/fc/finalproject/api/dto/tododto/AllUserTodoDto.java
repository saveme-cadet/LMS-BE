package com.larry.fc.finalproject.api.dto.tododto;

import com.larry.fc.finalproject.core.domain.entity.Todo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AllUserTodoDto {
    List<TodoDto> todoDtoList;
}
