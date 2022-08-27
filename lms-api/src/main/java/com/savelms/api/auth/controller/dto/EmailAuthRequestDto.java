package com.savelms.api.auth.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailAuthRequestDto {

    public static final String SIGNUP = "signup";
    public static final String RESET = "reset";
    @Schema(name = "id", example = "8414c5d1-5e09-4f76-8738-9c05fa603918")
    @NotNull
    private String id;

    @Schema(name = "email", example = "intraid@student.42seoul.kr")
    @NotNull
    private String email;

    @Schema(name = "authToken", example = "8414c5d1-5e09-4f76-8738-9c05fa603918")
    @NotNull
    private String authToken;

    @Schema(name = "authorizationType", example = "signup or reset")
    private String authorizationType = SIGNUP;



}
