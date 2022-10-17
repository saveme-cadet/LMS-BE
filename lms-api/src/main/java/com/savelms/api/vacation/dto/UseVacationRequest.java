package com.savelms.api.vacation.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class UseVacationRequest {

    @NotNull
    private Double usedDays;

    @NotNull
    private String reason;

    public UseVacationRequest() {}

    public UseVacationRequest(Double usedDays, String reason) {
        this.usedDays = usedDays;
        this.reason = reason;
    }
}
