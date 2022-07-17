package com.savelms.api.user.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequest {

    @Schema(name= "username" , example = "intraid")
    @NotNull
    private String username;

    @Schema(name= "password" , example = "asdfer222")
    @NotNull
    private String password;


}
