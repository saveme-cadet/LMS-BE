package com.savelms.api.todo.controller.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ListResponse<T> {

    private Integer count;

    private List<T> content = new ArrayList<>();
}
