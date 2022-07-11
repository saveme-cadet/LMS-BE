package com.savelms.api.vacation.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UseVacationRequest {

    @NotNull
    private Long usedDays;

    @NotNull
    private String reason;

}
