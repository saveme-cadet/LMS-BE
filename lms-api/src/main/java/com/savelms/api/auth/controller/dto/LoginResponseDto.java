package com.savelms.api.auth.controller.dto;

import com.savelms.core.user.role.RoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {

    private String id;
    private RoleEnum role;
}
