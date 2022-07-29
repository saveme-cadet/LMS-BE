package com.savelms.api.user.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class UserChangeAttendStatusResponse {

    @Schema(name = "userId" , example = "db688a4a-2f70-4265-a1ea-d15fd6c5c914")
    @NotBlank
    private String userId;
}
