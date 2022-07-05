package com.larry.fc.finalproject.api.controller.user;

import com.larry.fc.finalproject.api.dto.AuthUser;
import com.larry.fc.finalproject.api.service.LoginService;
import com.larry.fc.finalproject.api.service.UserStatisticalChartService;
import com.larry.fc.finalproject.api.service.userservice.AllUserTableService;
import com.larry.fc.finalproject.api.service.userservice.UserInfoService;
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

    private final LoginService loginService;
    private final UserInfoService userInfoService;
    private final UserStatisticalChartService userStatisticalChartService;
    private final AllUserTableService allUserTableService;

    //@PreAuthorize("hasAuthority('user.create')")
    @PostMapping("/users")
    public UserSignUpResponse signUp(@RequestBody UserSignUpRequest request) {

        User user = UserSignUpRequestToUser(request);
        Long signUpId = userService.validateUserNameAndCreate(user);
        userInfoService.create(AuthUser.of(signUpId));
        userStatisticalChartService.create(AuthUser.of(signUpId));
        allUserTableService.createUserDate(AuthUser.of(signUpId));
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
