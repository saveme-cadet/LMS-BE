package com.larry.fc.finalproject.api.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class SignUpReq {
    @NotBlank
    @Size(min = 1, max = 10)
    private final String name;
    private final String email;
    //private final String nickName;
    @NotBlank
    private final String password;
    private final LocalDate birthday;
}
