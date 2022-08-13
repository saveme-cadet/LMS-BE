package com.savelms.api.user.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignUpRequest {

    @Schema(name= "username" , example = "intraId로 무조건 입력해야함. 이메일인증에 활용. 입력하지 않으면 42 이메일 인증 불가")
    @NotNull
    private String username;

    @Schema(name= "password" , example = "asdfer222")
    @NotNull
    private String password;

//    @Schema(name= "email" , example = "test@gmail.com(이메일 인증에 사용되지 않음.)")
//    @Email(message = "이메일 형식이 올바르지 않습니다.")
//    private String email;
}
