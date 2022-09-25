package com.savelms.api.vacation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UseVacationRequest {

    @NotNull
    private Double usedDays;

    @NotNull
    private String reason;

}
