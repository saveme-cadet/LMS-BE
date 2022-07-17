package com.savelms.api.auth.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailAuthRequestDto {

    @Schema(name = "id", example = "8414c5d1-5e09-4f76-8738-9c05fa603918")
    @NotNull
    private String id;

    @Schema(name = "email", example = "intraid@student.42seoul.kr")
    @NotNull
    private String email;

    @Schema(name = "authToken", example = "8414c5d1-5e09-4f76-8738-9c05fa603918")
    @NotNull
    private String authToken;


}
