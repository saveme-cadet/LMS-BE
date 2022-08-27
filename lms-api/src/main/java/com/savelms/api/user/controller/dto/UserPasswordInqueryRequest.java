package com.savelms.api.user.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPasswordInqueryRequest {

    @Schema(name = "username", example = "intraId로 무조건 입력해야함. 이메일인증에 활용. 입력하지 않으면 42 이메일 인증 불가")
    @NotNull
    private String username;
}
