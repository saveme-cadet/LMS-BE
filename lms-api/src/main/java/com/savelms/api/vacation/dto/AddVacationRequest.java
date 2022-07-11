package com.savelms.api.vacation.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddVacationRequest {

    @NotNull
    private Long addedDays;

}
