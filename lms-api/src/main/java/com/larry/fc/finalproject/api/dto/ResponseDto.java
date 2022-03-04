package com.larry.fc.finalproject.api.dto;

import com.larry.fc.finalproject.api.dto.tabledto.AllUserTableDto;
import com.larry.fc.finalproject.api.dto.userinfodto.UserInfoDto;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data

public class ResponseDto <T>{
    private String error;
    private List<T> data;
    private List<UserInfoDto> userInfo;
    private List<AllUserTableDto> dayTables;
}