package com.savelms.api.vacation.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class AddVacationRequest {

    @NotNull
    private Double addedDays;

    public AddVacationRequest() {}

    public AddVacationRequest(Double addedDays) {
        this.addedDays = addedDays;
    }
}
