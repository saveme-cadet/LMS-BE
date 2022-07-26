package com.savelms.api.user.controller;

import com.savelms.api.todo.controller.dto.ListResponse;
import com.savelms.api.user.controller.dto.UserChangeAttendStatusRequest;
import com.savelms.api.user.controller.dto.UserChangeAttendStatusResponse;
import com.savelms.api.user.controller.dto.UserChangeRoleRequest;
import com.savelms.api.user.controller.dto.UserChangeRoleResponse;
import com.savelms.api.user.controller.dto.UserChangeTeamRequest;
import com.savelms.api.user.controller.dto.UserChangeTeamResponse;
import com.savelms.api.user.controller.dto.UserLoginRequest;
import com.savelms.api.user.controller.dto.UserParticipatingIdResponse;
import com.savelms.api.user.controller.dto.UserSignUpRequest;
import com.savelms.api.user.controller.dto.UserSignUpResponse;
import com.savelms.api.user.service.UserService;
import com.savelms.core.user.domain.DuplicateUsernameException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(originPatterns = "http://3.38.226.166:8080")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


//    @GetMapping("/users")
//    public UserSendUserListResponse sendUserList(
//        @RequestParam(value = "attendStatus", required = false, defaultValue = "true") Boolean attendStatus,
//        @RequestParam(value = "offset", required = false, defaultValue = "0") Long offset,
//        @RequestParam(value = "size", required = false, defaultValue="30") Long size,
//        @RequestParam(value = "sort", required = false, defaultValue = "nickname:asc") String sortRule) {
//
//        return userService.findUserList(attendStatus, offset, size, sortRule);
//    }

    @PreAuthorize("hasAuthority('user.read')")
    @GetMapping("/users/participating-this-month")
    public ListResponse<UserParticipatingIdResponse> sendParticipatingUserListUserId() {
        return userService.findParticipatingUserList();
    }

    @PreAuthorize("hasAuthority('user.create')")
    @PostMapping("/users")
    public ResponseEntity<UserSignUpResponse> signUp(@Validated @RequestBody UserSignUpRequest request) {

        String apiId = null;
        UserSignUpResponse response = null;
        try{
            apiId = userService.validateUserNameAndSignUp(request);
            response = new UserSignUpResponse();
            response.setId(apiId);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch( DuplicateUsernameException due){
            response = new UserSignUpResponse();
            response.setId(apiId);
            response.setError(due.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

    }

    @PreAuthorize("hasAuthority('user.team.update')")
    @PatchMapping("/users/{id}/team")
    public UserChangeTeamResponse changeTeam(@PathVariable("id") String apiId,
        @Validated @RequestBody UserChangeTeamRequest request) {

        return new UserChangeTeamResponse(userService.changeTeam(apiId, request));
    }


    @PreAuthorize("hasAuthority('user.role.update')")
    @PatchMapping("/users/{id}/role")
    public UserChangeRoleResponse changeRole(@PathVariable("id") String apiId,
        @Validated @RequestBody UserChangeRoleRequest request) {

        return new UserChangeRoleResponse(userService.changeRole(apiId, request));
    }

    @PreAuthorize("hasAuthority('user.attend-status.update')")
    @PatchMapping("/users/{id}/attendStatus")
    public UserChangeAttendStatusResponse changeAttendStatus(@PathVariable("id") String apiId,
        @Validated @RequestBody UserChangeAttendStatusRequest request) {

        return new UserChangeAttendStatusResponse(userService.changeAttendStatus(apiId, request));
    }

    @PostMapping("/auth/login")
    public String fakeLogin(@ModelAttribute UserLoginRequest request) {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }

    @PostMapping("/auth/logout")
    public void fakeLogout() {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }


}
