package com.savelms.api.auth.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmailAuthResponseDto {

    private String userId;
}
