package com.savelms.api.user.controller;

import com.savelms.api.user.controller.dto.UserChangeTeamRequest;
import com.savelms.api.user.controller.dto.UserChangeTeamResponse;
import com.savelms.api.user.controller.dto.UserSendUserListResponse;
import com.savelms.api.user.controller.dto.UserSignUpRequest;
import com.savelms.api.user.controller.dto.UserSignUpResponse;
import com.savelms.api.user.service.UserService;
import com.savelms.core.exception.QueryStringFormatException;
import com.savelms.core.user.role.RoleEnum;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/users")
    public UserSendUserListResponse sendUserList(
        @RequestParam(value = "attendStatus", required = false, defaultValue = "true") Boolean attendStatus,
        @RequestParam(value = "offset", required = false, defaultValue = "0") Long offset,
        @RequestParam(value = "size", required = false, defaultValue="30") Long size,
        @RequestParam(value = "sort", required = false, defaultValue = "nickname:asc") String sortRule) {

        return userService.findUserList(attendStatus, offset, size, sortRule);
    }



    //@PreAuthorize("hasAuthority('user.create')")
    @PostMapping("/users")
    public UserSignUpResponse signUp(@RequestBody UserSignUpRequest request) {

        String apiId = userService.validateUserNameAndSignUp(request);
        UserSignUpResponse response = new UserSignUpResponse();
        response.setId(apiId);
        return response;
    }

    @PatchMapping("/users/{id}/team")
    public UserChangeTeamResponse changeTeam(@PathVariable("id") String apiId,
        @RequestBody UserChangeTeamRequest request) {

        return new UserChangeTeamResponse(userService.changeTeam(apiId, request));
    }

}
