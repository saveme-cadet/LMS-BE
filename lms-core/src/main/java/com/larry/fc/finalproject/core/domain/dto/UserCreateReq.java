package com.larry.fc.finalproject.core.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class UserCreateReq {
    @NotBlank
    @Size(min = 1, max = 10)
    private final String name;
    private final String email;
    @NotBlank
    private final String password;
    private final LocalDate birthday;
//    private final Long attendStatus;
}
