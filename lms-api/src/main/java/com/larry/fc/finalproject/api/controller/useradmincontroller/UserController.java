package com.larry.fc.finalproject.api.controller.useradmincontroller;


import com.larry.fc.finalproject.api.dto.userdto.UserDto;
import com.larry.fc.finalproject.api.service.userservice.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserQueryService userQueryService;

    @Operation(description = "참가 하는 모든 유저 Id 가져 오기")
    @GetMapping("/api/alluser")
    public List<UserDto> allUserGet(){
        return userQueryService.getUser();
    }

}
