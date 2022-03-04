package com.larry.fc.finalproject.api.dto;

import lombok.Data;

@Data
public class LoginReq {
    private final String email;
    private final String password;
}
