package com.savelms.api.user.controller.dto;

import com.savelms.core.user.role.RoleEnum;
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
public class UserChangeRoleRequest {

    @Schema(name= "role" , example = "UNAUTHORIZED, USER, MANAGER, ADMIN")
    @NotNull
    RoleEnum role;

    @Schema(name= "reason" , example = "role변경 사유 입력")
    String reason;
}
