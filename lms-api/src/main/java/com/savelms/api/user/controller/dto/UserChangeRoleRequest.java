package com.savelms.api.user.controller.dto;

import com.savelms.core.user.role.RoleEnum;
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
public class UserChangeRoleRequest {

    RoleEnum role;
    String reason;
}
