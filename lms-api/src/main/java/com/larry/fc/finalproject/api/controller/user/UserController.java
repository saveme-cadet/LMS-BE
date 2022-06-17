package com.larry.fc.finalproject.api.controller.user;

import com.larry.fc.finalproject.core.domain.entity.user.User;
import com.larry.fc.finalproject.core.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(originPatterns = "http://3.38.226.166:8080")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //@PreAuthorize("hasAuthority('user.create')")
    @PostMapping("/users")
    public UserSignUpResponse signUp(@RequestBody UserSignUpRequest request) {

        User user = UserSignUpRequestToUser(request);
        Long signUpId = userService.validateUserNameAndCreate(user);
        UserSignUpResponse response = new UserSignUpResponse();
        response.setUserId(signUpId);
        return response;
    }

    private User UserSignUpRequestToUser(UserSignUpRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        return user;
    }
}
