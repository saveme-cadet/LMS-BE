package com.savelms.api.user.controller;

import com.savelms.api.todo.controller.dto.ListResponse;
import com.savelms.api.user.controller.dto.UserChangePasswordRequest;
import com.savelms.api.user.controller.dto.UserPasswordInqueryRequest;
import com.savelms.api.user.controller.dto.UserChangeAttendStatusRequest;
import com.savelms.api.user.controller.dto.UserChangeAttendStatusResponse;
import com.savelms.api.user.controller.dto.UserChangeRoleRequest;
import com.savelms.api.user.controller.dto.UserChangeRoleResponse;
import com.savelms.api.user.controller.dto.UserChangeTeamRequest;
import com.savelms.api.user.controller.dto.UserChangeTeamResponse;
import com.savelms.api.user.controller.dto.UserLoginRequest;
import com.savelms.api.user.controller.dto.UserParticipatingIdResponse;
import com.savelms.api.user.controller.dto.UserResponseDto;
import com.savelms.api.user.controller.dto.UserSignUpRequest;
import com.savelms.api.user.controller.dto.UserSignUpResponse;
import com.savelms.api.user.controller.error.ErrorResult;
import com.savelms.api.user.service.UserService;
import com.savelms.core.user.AttendStatus;
import com.savelms.core.user.domain.DuplicateUsernameException;
import com.savelms.core.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Parameter;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    @ExceptionHandler
    public ResponseEntity<ErrorResult> duplicateUsernameException(DuplicateUsernameException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ErrorResult.builder()
                .message(e.getMessage())
                .build());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> entityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResult.builder()
                .message(e.getMessage())
                .build());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> checkPasswordUnequal(RequestRejectedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResult.builder()
                .message(e.getMessage())
                .build());
    }




    private final UserService userService;

    @PreAuthorize("hasAuthority('user.read')")
    @GetMapping("/users")
    public ListResponse<UserResponseDto> sendUserList(
        @RequestParam(value = "offset", required = false, defaultValue = "0") Long offset,
        @RequestParam(value = "size", required = false, defaultValue = "100") Long size
    ) {
        return userService.findUserList(offset, size);
    }

    @PreAuthorize("hasAuthority('user.read')")
    @GetMapping("/users/participating-this-month")
    public ListResponse<UserParticipatingIdResponse> sendParticipatingUserListUserId() {
        return userService.findParticipatingUserList();
    }

    //@PreAuthorize("hasAuthority('user.create')")
    @PostMapping("/users")
    public ResponseEntity<UserSignUpResponse> signUp(
        @Validated @RequestBody UserSignUpRequest request) {

        String apiId = null;
        UserSignUpResponse response = new UserSignUpResponse();
        try{
            apiId = userService.validateUserNameAndSignUp(request);
        } catch(DataIntegrityViolationException e) {
            if(e.getCause() instanceof ConstraintViolationException) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
        }
        response.setId(apiId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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

    /**
     *
     * @param apiId
     * @param request
     * @return
     *
     *      @PreAuthorize("hasAuthority('user.attend-status.update')")
     *      주석 처리
     */
    @PreAuthorize("hasAuthority('user.attend-status.update')")
    @PatchMapping("/users/{id}/attendStatus")
    public UserChangeAttendStatusResponse changeAttendStatus(@PathVariable("id") String apiId,
        @Validated @RequestBody UserChangeAttendStatusRequest request) {

        return new UserChangeAttendStatusResponse(userService.changeAttendStatus(apiId, request));
    }

    @PostMapping("/auth/login")
    public String fakeLogin(@Validated @ModelAttribute UserLoginRequest request) {
        throw new IllegalStateException(
            "This method shouldn't be called. It's implemented by Spring Security filters.");
    }

    @PostMapping("/auth/logout")
    public void fakeLogout() {
        throw new IllegalStateException(
            "This method shouldn't be called. It's implemented by Spring Security filters.");
    }


    @PreAuthorize("hasAuthority('user.update')")
    @PatchMapping("/auth/password")
    public ResponseEntity<Void> changePassword(
        @Validated @Parameter @RequestBody UserChangePasswordRequest request,
        @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        if (request.getPassword().equals(request.getCheckPassword()) == false) {
            throw new RequestRejectedException("확인 비밀번호가 다릅니다.");
        }
        userService.changePassword(user.getUsername(), request);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
