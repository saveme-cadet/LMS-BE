package com.larry.fc.finalproject.api.dto.tododto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RequestTodoDto {
    private final Long writerId;
    private final LocalDate date;
}
