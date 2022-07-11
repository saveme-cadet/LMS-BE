package com.savelms.api.vacation.dto;

import com.savelms.core.vacation.domain.entity.Vacation;
import lombok.Data;


@Data
public class VacationReasonResponse {

    private Long remainingDays;

    public VacationReasonResponse(Vacation vacation) {
        this.remainingDays = vacation.getRemainingDays();
    }
}
