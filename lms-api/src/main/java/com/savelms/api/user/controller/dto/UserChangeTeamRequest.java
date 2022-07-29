package com.savelms.api.user.controller.dto;

import com.savelms.core.team.TeamEnum;
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
public class UserChangeTeamRequest {


    @Schema(name= "team" , example = "RED, BLUE, NONE")
    @NotNull
    TeamEnum team;

    @Schema(name= "reason" , example = "team 변경사유 입력")
    String reason;
}
