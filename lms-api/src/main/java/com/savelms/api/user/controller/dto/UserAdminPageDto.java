package com.savelms.api.user.controller.dto;

import com.savelms.core.user.AttendStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class UserAdminPageDto {

    @Schema(name = "userId" , example = "db688a4a-2f70-4265-a1ea-d15fd6c5c914")
    @NotBlank
    String apiId;

    @Schema(description = "참여 여부" , example = "PARTICIPATED, NOT_PARTICIPATED")
    @NotNull
    AttendStatus attendStatus;

    @Schema(description = "참여자 이름" , example = "sjin")
    @NotBlank
    String nickname;
}
