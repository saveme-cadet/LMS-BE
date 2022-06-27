package com.savelms.api.user.controller;

import com.savelms.api.user.controller.dto.UserSignUpRequest;
import com.savelms.api.user.controller.dto.UserSignUpResponse;
import com.savelms.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //@PreAuthorize("hasAuthority('user.create')")
    @PostMapping("/users")
    public UserSignUpResponse signUp(@RequestBody UserSignUpRequest request) {

        Long signUpId = userService.validateUserNameAndSignUp(request);
        UserSignUpResponse response = new UserSignUpResponse();
        response.setUserId(signUpId);
        return response;
    }

}
