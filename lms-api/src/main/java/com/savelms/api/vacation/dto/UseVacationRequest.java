package com.savelms.api.vacation.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UseVacationRequest {

    @NotNull
    private Double usedDays;

    @NotNull
    private String reason;

}
