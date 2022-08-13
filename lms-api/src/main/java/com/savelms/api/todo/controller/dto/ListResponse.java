package com.savelms.api.todo.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
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
public class ListResponse<T> {

    @Schema(name= "count" , example = "3")
    @NotNull
    private Integer count;

    @Schema(name= "내용물 list로 반환" , example = "[{}, {}, {}]")
    @NotNull
    private List<T> content = new ArrayList<>();
}
