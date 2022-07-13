package com.savelms.api.vacation.dto;

import com.savelms.core.vacation.domain.entity.Vacation;
import lombok.Data;

import java.time.format.DateTimeFormatter;


@Data
public class VacationReasonResponse {

    private String usedDate;
    private Long usedDays;
    private String reason;

    public VacationReasonResponse(Vacation vacation) {
        this.usedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(vacation.getCreatedAt());
        this.usedDays = vacation.getUsedDays();
        this.reason = vacation.getReason();
    }

}
