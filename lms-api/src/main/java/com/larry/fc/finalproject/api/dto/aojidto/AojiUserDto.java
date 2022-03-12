package com.larry.fc.finalproject.api.dto.aojidto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AojiUserDto {
    private final String name;
    private final String team;
}
