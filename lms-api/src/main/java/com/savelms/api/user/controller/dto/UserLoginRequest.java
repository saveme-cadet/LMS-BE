package com.savelms.api.user.controller.dto;

import com.savelms.api.user.passwordvalidator.annotation.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequest {

    @Schema(name= "username" , example = "intraid")
    @NotBlank
    private String username;

    @Schema(name= "password" , example = "영어 대문자 + 영어 소문자 + 특수문자 + 길이 8~30")
    @NotBlank
    @ValidPassword
    private String password;


}
