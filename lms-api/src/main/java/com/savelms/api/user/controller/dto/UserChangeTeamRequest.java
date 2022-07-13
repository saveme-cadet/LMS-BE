package com.savelms.api.user.controller.dto;

import com.savelms.core.team.TeamEnum;
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

    TeamEnum team;
    String reason;
}
