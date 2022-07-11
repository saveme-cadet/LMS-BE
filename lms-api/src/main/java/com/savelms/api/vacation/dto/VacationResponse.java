package com.savelms.api.vacation.dto;

import com.savelms.core.vacation.domain.entity.Vacation;
import lombok.Data;

@Data
public class VacationResponse {

    private Long remainingDays;
    private Long usedDays;
    private String reason;

    public VacationResponse(Vacation vacation) {
        this.remainingDays = vacation.getRemainingDays();
        this.usedDays = vacation.getUsedDays();
        this.reason = vacation.getReason();
    }

}
