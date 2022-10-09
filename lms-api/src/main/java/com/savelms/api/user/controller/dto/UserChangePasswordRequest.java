package com.savelms.api.user.controller.dto;

import com.savelms.api.user.passwordvalidator.annotation.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserChangePasswordRequest {

    @Schema(name= "oldPassword" , example = "영어 대문자 + 영어 소문자 + 특수문자 + 길이 8~30, 현재 비밀번호")
    @NotBlank
    @ValidPassword
    private String oldPassword;

    @Schema(name= "password" , example = "영어 대문자 + 영어 소문자 + 특수문자 + 길이 8~30, 바꾸려는 비밀번호")
    @NotBlank
    @ValidPassword
    private String password;

    @Schema(name= "checkPassword" , example = "영어 대문자 + 영어 소문자 + 특수문자 + 길이 8~30, 비밀번호 확인용")
    @NotBlank
    @ValidPassword
    private String checkPassword;
}
