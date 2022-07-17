package com.savelms.api.user.controller;

import com.savelms.api.todo.controller.dto.ListResponse;
import com.savelms.api.user.controller.dto.UserChangeAttendStatusRequest;
import com.savelms.api.user.controller.dto.UserChangeAttendStatusResponse;
import com.savelms.api.user.controller.dto.UserChangeRoleRequest;
import com.savelms.api.user.controller.dto.UserChangeRoleResponse;
import com.savelms.api.user.controller.dto.UserChangeTeamRequest;
import com.savelms.api.user.controller.dto.UserChangeTeamResponse;
import com.savelms.api.user.controller.dto.UserParticipatingIdResponse;
import com.savelms.api.user.controller.dto.UserSendUserListResponse;
import com.savelms.api.user.controller.dto.UserSignUpRequest;
import com.savelms.api.user.controller.dto.UserSignUpResponse;
import com.savelms.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(originPatterns = "http://3.38.226.166:8080")
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

    @GetMapping("/users/participating-this-month")
    public ListResponse<UserParticipatingIdResponse> sendParticipatingUserListUserId() {
        return userService.findParticipatingUserList();
    }

    //@PreAuthorize("hasAuthority('user.create')")
    @PostMapping("/users")
    public UserSignUpResponse signUp(@Validated @RequestBody UserSignUpRequest request) {

        String apiId = userService.validateUserNameAndSignUp(request);
        UserSignUpResponse response = new UserSignUpResponse();
        response.setId(apiId);
        return response;
    }

    @PatchMapping("/users/{id}/team")
    public UserChangeTeamResponse changeTeam(@PathVariable("id") String apiId,
        @Validated @RequestBody UserChangeTeamRequest request) {

        return new UserChangeTeamResponse(userService.changeTeam(apiId, request));
    }


    @PatchMapping("/users/{id}/role")
    public UserChangeRoleResponse changeRole(@PathVariable("id") String apiId,
        @Validated @RequestBody UserChangeRoleRequest request) {

        return new UserChangeRoleResponse(userService.changeRole(apiId, request));
    }

    @PatchMapping("/users/{id}/attendStatus")
    public UserChangeAttendStatusResponse changeAttendStatus(@PathVariable("id") String apiId,
        @Validated @RequestBody UserChangeAttendStatusRequest request) {

        return new UserChangeAttendStatusResponse(userService.changeAttendStatus(apiId, request));
    }


}
