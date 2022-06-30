package com.savelms.api.user.controller.dto;

import com.savelms.core.user.domain.entity.User;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserSendUserListResponse {

    public Integer count;
    public List<User> users;
}
