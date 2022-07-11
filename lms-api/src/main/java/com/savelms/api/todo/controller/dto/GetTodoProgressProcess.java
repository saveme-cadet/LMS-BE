package com.savelms.api.todo.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetTodoProgressResponse {

    @Schema(name= "writerId" , example = "d1775d2f-7347-4536-a816-8b38d01d5ce4")
    @NotNull
    @NotBlank
    private String writerId;

    @Schema(name= "writerId" , example = "0.5")
    private Double progress;



}
