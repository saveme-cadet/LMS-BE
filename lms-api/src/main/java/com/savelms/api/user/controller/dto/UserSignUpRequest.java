package com.savelms.api.user.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignUpRequest {

    private String username;
    private String password;
    private String email;
}
