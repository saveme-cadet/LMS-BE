package com.larry.fc.finalproject.api.dto.userdto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDto {

    private final String userName;
    private final Long userId;
}
