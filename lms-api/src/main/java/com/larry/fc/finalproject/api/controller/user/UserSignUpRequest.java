package com.larry.fc.finalproject.api.controller.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignUpRequest {

    private String username;
    private String password;
    private String email;
}
