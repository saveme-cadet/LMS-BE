package com.larry.fc.finalproject.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class AuthUser {
    @Schema(name = "userId")
    private final Long id;

    private AuthUser(Long id){
        this.id = id;
    }

    public static AuthUser of(Long id){
        return new AuthUser(id);
    }
}
