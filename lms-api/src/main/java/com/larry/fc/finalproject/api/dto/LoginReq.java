package com.larry.fc.finalproject.api.dto;

import lombok.Data;

@Data
public class LoginReq {
    private final String name;
    private final String password;
}
