package com.savelms.api.vacation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class AddVacationRequest {

    @NotNull
    private Double addedDays;

}
