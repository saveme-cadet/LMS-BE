package com.savelms.api.user.controller.dto;

import com.savelms.core.user.domain.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserSendUserListResponse {

    @Schema(description = "요소 개수" , example = "3")
    @NotNull
    public Integer count;

    @Schema(description = "요소 리스트" , example = "[{}, {}, {}]")
    public List<UserResponseDto> users;
}
