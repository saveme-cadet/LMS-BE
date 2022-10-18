package com.savelms.api.user.controller.dto;

import com.savelms.api.user.passwordvalidator.annotation.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserSignUpRequest {

    @Schema(name= "username" , example = "intraId로 무조건 입력해야함. 이메일인증에 활용. 입력하지 않으면 42 이메일 인증 불가")
    @NotNull
    private String username;

    @Schema(name= "password" , example = "영어 대문자 + 영어 소문자 + 특수문자 + 길이 8~30")
    @NotNull
    @ValidPassword
    private String password;

}
