package com.savelms.api.vacation.dto;

import com.savelms.core.vacation.domain.entity.Vacation;
import lombok.Data;

@Data
public class VacationResponse {

    private Double remainingDays;

    public VacationResponse(Vacation vacation) {
        this.remainingDays = vacation.getRemainingDays();
    }
}
