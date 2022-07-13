package com.savelms.api.user.controller.dto;

import com.savelms.core.user.AttendStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserChangeAttendStatusRequest {
    @Schema(name= "attendStatus" , example = "PARTICIPATED, NOT_PARTICIPATED")
    @NotNull
    AttendStatus attendStatus;
}
