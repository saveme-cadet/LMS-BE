package com.savelms.api.user.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserChangeRoleRequest {

    String role;
}
